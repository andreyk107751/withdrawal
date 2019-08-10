package com.demo.withdrawal.controller;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.demo.withdrawal.model.SettlementMessage;
import com.demo.withdrawal.model.User;
import com.demo.withdrawal.model.WithdrawalResult;
import com.demo.withdrawal.service.KafkaListenerService;
import com.demo.withdrawal.service.WithdrawalService;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@Tag("integration")
@ActiveProfiles("test")
class WithdrawalControllerTest {

	@Autowired
	private WithdrawalController controller;

	@Autowired
	private WithdrawalService service;
	
	private String message = "{ “order_id”:”1”, “user_id”:”200”, " + 
			"“bought_token”:”ETH”, “bought_quantity”:”87.35”, " + 
			"“sold_token”:BTC”, “sold_quantity”:”6.9” }";

	@Autowired
	private KafkaListenerService kafkaListenerService;

	@Test
	void testWithdraw() {
		WithdrawalResult result;

		result = controller.withdraw(1, 1, "USD", "1");
		assertEquals(WithdrawalResult.INSUFFICIENT_BALANCE, result);
		
		result = controller.withdraw(1, 1, "EUR", "100");
		assertEquals(WithdrawalResult.INSUFFICIENT_BALANCE, result);
		
		result = controller.withdraw(1, 1, "USD", "13432");
		assertEquals(WithdrawalResult.INSUFFICIENT_BALANCE, result);
		
		result = controller.withdraw(1, 1, "ETH", "12342");
		assertEquals(WithdrawalResult.INSUFFICIENT_BALANCE, result);
		
		result = controller.withdraw(1, 1, "NA", "16565");
		assertEquals(WithdrawalResult.INSUFFICIENT_BALANCE, result);
		
		result = controller.withdraw(1, 100, "USD", "14564");
		assertEquals(WithdrawalResult.INSUFFICIENT_BALANCE, result);
		
		result = controller.withdraw(1, 1111, "USD", "14564");
		assertEquals(WithdrawalResult.INSUFFICIENT_BALANCE, result);
		
		result = controller.withdraw(1, 100, "EUR", "156");
		assertEquals(WithdrawalResult.SUFFICIENT_BALANCE, result);
						
		result = controller.withdraw(12, 100, "USD", "250");
		assertEquals(WithdrawalResult.INSUFFICIENT_BALANCE, result);
		
		result = controller.withdraw(13, 100, "BTC", "7");
		assertEquals(WithdrawalResult.SUFFICIENT_BALANCE, result);
	}

	/*
	 * User 200:
	 * ID,	USD,		EUR,		BTC,		BCH,		ETH
	 * 200,	68978.69,	7831.87,	79.779391,	165.360463,	71.092086
	 */
	@Test
	void testSettle() throws IOException {
		WithdrawalResult result;
		result = controller.withdraw(15, 200, "BTC", "6.9");
		assertEquals(WithdrawalResult.SUFFICIENT_BALANCE, result);
		
		User user = service.loadFromCache(200);
		
		SettlementMessage settlementMessage = kafkaListenerService.parseMessage(message);
		// + ETH 87.35
		service.settleOrder(settlementMessage);
		
		assertEquals(user.getHoldingsBtc().compareTo(new BigDecimal("72.879391")), 0);
		assertEquals(user.getHoldingsEth().compareTo(new BigDecimal("158.442086")), 0);
		
	}
	
}
