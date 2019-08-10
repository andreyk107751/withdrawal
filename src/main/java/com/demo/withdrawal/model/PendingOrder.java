package com.demo.withdrawal.model;

import java.math.BigDecimal;

public class PendingOrder {

	private final Integer orderId;
	private final Tokens token;
	private final BigDecimal amount;
	
	public PendingOrder(Integer orderId, Tokens token, BigDecimal amount) {
		this.orderId = orderId;
		this.token = token;
		this.amount = amount;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public Tokens getToken() {
		return token;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	
}
