package com.demo.withdrawal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.withdrawal.model.WithdrawalResult;
import com.demo.withdrawal.service.WithdrawalService;
import com.demo.withdrawal.util.ValidationUtils;

@RestController
public class WithdrawalController {
	Logger logger = LoggerFactory.getLogger(WithdrawalController.class);
	
	@Autowired
	private WithdrawalService withdrawalService;
	
    @RequestMapping("/withdraw")
    public WithdrawalResult withdraw(@RequestParam(value="orderId") Integer orderId,
    		@RequestParam(value="userId") Integer userId,
    		@RequestParam(value="token") String token,
    		@RequestParam(value="amount") String amount) {
    	logger.info("withdraw; userId: {}, token: {}, amount: {}", new Object[] {userId, token, amount}); 
    	try {
    		ValidationUtils.validateParams(orderId, userId, token, amount);
    		return withdrawalService.withdraw(orderId, userId, token, amount);
    	}catch(Throwable e) {
    		return WithdrawalResult.INSUFFICIENT_BALANCE;
    	}
    }
}