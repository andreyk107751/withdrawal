package com.demo.withdrawal.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import com.demo.withdrawal.model.WithdrawalResult;
import com.demo.withdrawal.util.ValidationUtils;

public class User {
	private Integer id;
	private BigDecimal holdingsUsd;
	private BigDecimal holdingsEur;
	private BigDecimal holdingsBtc;
	private BigDecimal holdingsBch;
	private BigDecimal holdingsEth;
	private Map<String, PendingOrder> pendingOrders = new HashMap<>();

	private User() {}
	
	public static User createUser(String line) {
		User user = new User();
		String[] values = line.split(",");
		user.id = Integer.parseInt(values[0]);
		user.holdingsUsd = new BigDecimal(values[1]);
		user.holdingsEur = new BigDecimal(values[2]);
		user.holdingsBtc = new BigDecimal(values[3]);
		user.holdingsBch = new BigDecimal(values[4]);
		user.holdingsEth = new BigDecimal(values[5]);
		return user;
	}

	public synchronized WithdrawalResult withdraw(Integer orderId, Tokens token, BigDecimal amount) {
		PendingOrder order = new PendingOrder(orderId, token, amount);
		switch(token) {
			case USD:
				ValidationUtils.assureAvailableBalance(this.holdingsUsd, amount);
				this.holdingsUsd = this.holdingsUsd.subtract(amount);
			    break;
			case EUR:
				ValidationUtils.assureAvailableBalance(this.holdingsEur, amount);
				this.holdingsEur = this.holdingsEur.subtract(amount);
			    break;
			case BTC:
				ValidationUtils.assureAvailableBalance(this.holdingsBtc, amount);
				this.holdingsBtc = this.holdingsBtc.subtract(amount);
			    break;
			case BCH:
				ValidationUtils.assureAvailableBalance(this.holdingsBch, amount);
				this.holdingsBch = this.holdingsBch.subtract(amount);
			    break;
			case ETH:
				ValidationUtils.assureAvailableBalance(this.holdingsEth, amount);
				this.holdingsEth = this.holdingsEth.subtract(amount);
			    break;
			default:
			    break;
		}
		pendingOrders.put(String.valueOf(order), order);
		return WithdrawalResult.SUFFICIENT_BALANCE;
	}
	
	public Integer getId() {
		return id;
	}

	public BigDecimal getHoldingsUsd() {
		return holdingsUsd;
	}

	public BigDecimal getHoldingsEur() {
		return holdingsEur;
	}

	public BigDecimal getHoldingsBtc() {
		return holdingsBtc;
	}

	public BigDecimal getHoldingsBch() {
		return holdingsBch;
	}

	public BigDecimal getHoldingsEth() {
		return holdingsEth;
	}

	public void updateHoldings(Integer orderId, Tokens tokenSold, BigDecimal amountSold, Tokens tokenBought, BigDecimal amountBought) {
		PendingOrder pendingOrder = pendingOrders.get(String.valueOf(orderId));
		
		//TODO: more checks to make sure the order is what was intended, out of scope for this exercise
		
		if(pendingOrder.getAmount().compareTo(amountSold) != 0) {
			//simply add the bought amount to holdings
			adjustSoldQuantity(tokenSold, pendingOrder.getAmount(), amountSold);
		} 
		addToHoldings(tokenBought, amountBought);
		pendingOrders.remove(String.valueOf(orderId));					
	}

	private void adjustSoldQuantity(Tokens tokenSold, BigDecimal amount, BigDecimal amountSold) {
		switch(tokenSold) {
			case USD:
				this.holdingsUsd = this.holdingsUsd.add(this.holdingsUsd.subtract(amountSold));
			    break;
			case EUR:
				this.holdingsEur = this.holdingsEur.add(this.holdingsEur.subtract(amountSold));
			    break;
			case BTC:
				this.holdingsBtc = this.holdingsBtc.add(this.holdingsBtc.subtract(amountSold));
			    break;
			case BCH:
				this.holdingsBch = this.holdingsBch.add(this.holdingsBch.subtract(amountSold));
			    break;
			case ETH:
				this.holdingsEth = this.holdingsEth.add(this.holdingsEth.subtract(amountSold));
			    break;
			default:
			    break;
		}
	}

	private void addToHoldings(Tokens tokenBought, BigDecimal amountBought) {
		switch(tokenBought) {
			case USD:
				this.holdingsUsd = this.holdingsUsd.add(amountBought);
			    break;
			case EUR:
				this.holdingsEur = this.holdingsEur.add(amountBought);
			    break;
			case BTC:
				this.holdingsBtc = this.holdingsBtc.add(amountBought);
			    break;
			case BCH:
				this.holdingsBch = this.holdingsBch.add(amountBought);
			    break;
			case ETH:
				this.holdingsEth = this.holdingsEth.add(amountBought);
			    break;
			default:
			    break;
		}
		
	}
	
	
}
