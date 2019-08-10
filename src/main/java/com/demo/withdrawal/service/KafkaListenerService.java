package com.demo.withdrawal.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.demo.withdrawal.model.SettlementMessage;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaListenerService {
	Logger logger = LoggerFactory.getLogger(KafkaListenerService.class);

	@Autowired
	private WithdrawalService withdrawalService;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@KafkaListener(topics = "test", groupId = "testGroup")
	public void listen(String message) {
		logger.info("Received Messasge: {}", message);
	    SettlementMessage settlementMessage;
		try {
			settlementMessage = parseMessage(message);
		    withdrawalService.settleOrder(settlementMessage);
		} catch (IOException e) {
			// TODO: raise alert on bad message
			e.printStackTrace();
		} 
	    
	}

	public SettlementMessage parseMessage(String message) throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(message, SettlementMessage.class);
	}

}
