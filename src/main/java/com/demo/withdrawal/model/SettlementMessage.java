package com.demo.withdrawal.model;

public class SettlementMessage {

	private Integer order_id;
	private Integer user_id;
	private String bought_token;
	private String bought_quantity;
	private String sold_token;
	private String sold_quantity;
	
	
	public Integer getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getBought_token() {
		return bought_token;
	}
	public void setBought_token(String bought_token) {
		this.bought_token = bought_token;
	}
	public String getBought_quantity() {
		return bought_quantity;
	}
	public void setBought_quantity(String bought_quantity) {
		this.bought_quantity = bought_quantity;
	}
	public String getSold_token() {
		return sold_token;
	}
	public void setSold_token(String sold_token) {
		this.sold_token = sold_token;
	}
	public String getSold_quantity() {
		return sold_quantity;
	}
	public void setSold_quantity(String sold_quantity) {
		this.sold_quantity = sold_quantity;
	}
	
	
//	{ “user_id”:”100”, 
//	“bought_token”:”ETH”, 
//	“bought_quantity”:”87.35”, 
//	“sold_token”:BTC”, 
//	“sold_quantity”:”6.9” }

}
