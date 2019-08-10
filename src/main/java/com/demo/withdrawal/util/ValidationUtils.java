package com.demo.withdrawal.util;

import java.math.BigDecimal;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.withdrawal.model.Tokens;

public class ValidationUtils {
	private static Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

	public static final Predicate<Integer> isValidIntParam = i -> i != null && i > 0;
	public static final Predicate<String> isValidStringParam = str -> !StringUtils.isBlank(str);
	public static final Predicate<String> isValidStringNumberParam = str -> new BigDecimal(str).compareTo(BigDecimal.ZERO) > 0;
	public static final Predicate<String> isValidTokenParam = str -> Tokens.valueOf(str) != null; // will throw if invalid
	
	public static void validateParams(Integer orderId, Integer userId, String token, String amount) {
		if(isValidIntParam.test(orderId) &&
				isValidIntParam.test(userId) &&
				isValidStringParam.test(token) &&
				isValidStringParam.test(amount) &&
				isValidStringNumberParam.test(amount) &&
				isValidTokenParam.test(token)) {
			logger.info("parameters validated");
		} else {
			throw new IllegalArgumentException("invalid parameter");
		}
	}

	public static void assureAvailableBalance(BigDecimal holdings, BigDecimal amount) {
		if(holdings.compareTo(BigDecimal.ZERO) == 0 || holdings.compareTo(amount) < 0) 
			throw new IllegalStateException("balance is too low");
	}

	public static void validateOrderParameters(Integer order_id, Integer user_id, String sold_token,
			String sold_quantity, String bought_token, String bought_quantity) {
		if(isValidIntParam.test(order_id) &&
				isValidIntParam.test(user_id) &&
				isValidTokenParam.test(sold_token) &&
				isValidTokenParam.test(bought_token) &&
				isValidStringNumberParam.test(sold_quantity) &&
				isValidStringNumberParam.test(bought_quantity)) {
			logger.info("order parameters validated");
		} else {
			throw new IllegalArgumentException("invalid parameter");
		}
		
	}
}
