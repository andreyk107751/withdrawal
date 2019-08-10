package com.demo.withdrawal.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.demo.withdrawal.model.SettlementMessage;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@Tag("integration")
@ActiveProfiles("test")
class KafkaListenerServiceTest {

	private String message = "{ “order_id”:”1”, “user_id”:”100”, " + 
			"“bought_token”:”ETH”, “bought_quantity”:”87.35”, " + 
			"“sold_token”:BTC”, “sold_quantity”:”6.9” }";

	@Autowired
	private KafkaListenerService service;
	
	@Test
	void testGenerateMessageFromString() throws JsonParseException, JsonMappingException, IOException {
		
		SettlementMessage settlementMessage = service.parseMessage(message);
		
		assertEquals(settlementMessage.getUser_id(), 100);
		
		//TODO: test other variables are correctly set
	}

}
