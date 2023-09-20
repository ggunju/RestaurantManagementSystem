package com.example.demo.entity;

public class TransactionDetails {
	private String rorderId;
	private String currency;
	private Integer amount;
	private String key;

	public TransactionDetails(String rorderId, String currency, Integer amount, String key) {
		super();
		this.rorderId = rorderId;
		this.currency = currency;
		this.amount = amount;
		this.key = key;
	}

	public String getRorderId() {
		return rorderId;
	}

	public void setRorderId(String rorderId) {
		this.rorderId = rorderId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}