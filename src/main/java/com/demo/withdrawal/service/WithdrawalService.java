package com.demo.withdrawal.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.demo.withdrawal.model.SettlementMessage;
import com.demo.withdrawal.model.Tokens;
import com.demo.withdrawal.model.User;
import com.demo.withdrawal.model.WithdrawalResult;
import com.demo.withdrawal.util.LruCache;
import com.demo.withdrawal.util.ValidationUtils;

@Service
public class WithdrawalService {

	private static final Map<String, User> cache = Collections.synchronizedMap(LruCache.newInstance());
	private static final String filePath = "C:\\temp\\db.csv";
	
	@PostConstruct
	public void init() throws IOException {
		loadInitialUsers();
	}
	
	private void loadInitialUsers() throws IOException {
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			stream.skip(2)
				.limit(LruCache.CACHE_SIZE)
				.map(line -> User.createUser(line))
				.forEach(WithdrawalService::addToCache);
		}
	}

	public static void addToCache(User user) {
		cache.put(String.valueOf(user.getId()), user);
	}
	
	public WithdrawalResult withdraw(Integer orderId, Integer userId, String token, String amount) throws IOException {
		User user = cache.get(String.valueOf(userId));
		if(user == null) {
			user = loadFromStore(userId);
			if(user != null) 
				cache.put(String.valueOf(user.getId()), user);
		}
		return user == null ? WithdrawalResult.INSUFFICIENT_BALANCE : 
			user.withdraw(orderId, Tokens.valueOf(token), new BigDecimal(amount));
	}
	
	public static User loadFromCache(Integer userId) throws IOException {
		return cache.get(String.valueOf(userId));
	}
	
	public static User loadFromStore(Integer userId) throws IOException {
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			String s = stream.filter(line -> line.startsWith(String.valueOf(userId) + ","))
				.findFirst().get();
			return s == null ? null : User.createUser(s);
		}
	}

	public void settleOrder(SettlementMessage settlementMessage) {
		ValidationUtils.validateOrderParameters(settlementMessage.getOrder_id(),
				settlementMessage.getUser_id(),
				settlementMessage.getSold_token(), 
				settlementMessage.getSold_quantity(), 
				settlementMessage.getBought_token(), 
				settlementMessage.getBought_quantity());
		User user = cache.get(String.valueOf(settlementMessage.getUser_id()));
		
		Tokens tokenSold = Tokens.valueOf(settlementMessage.getSold_token());
		BigDecimal amountSold = new BigDecimal(settlementMessage.getSold_quantity());

		Tokens tokenBought = Tokens.valueOf(settlementMessage.getBought_token());
		BigDecimal amountBought = new BigDecimal(settlementMessage.getBought_quantity());
				
		user.updateHoldings(settlementMessage.getOrder_id(), tokenSold, amountSold, tokenBought, amountBought);
	}

}
