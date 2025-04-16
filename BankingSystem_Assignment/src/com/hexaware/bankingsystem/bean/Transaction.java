package com.hexaware.bankingsystem.bean;

import java.sql.Timestamp;

public class Transaction {
    private long accountNumber;
    private String description;
    private Timestamp dateTime;
    private String transactionType;
    private float amount;
    
    
	public Transaction(long accountNumber, String description, Timestamp dateTime, String transactionType,
			float amount) {
		super();
		this.accountNumber = accountNumber;
		this.description = description;
		this.dateTime = dateTime;
		this.transactionType = transactionType;
		this.amount = amount;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getDateTime() {
		return dateTime;
	}
	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "Transaction [accountNumber=" + accountNumber + ", description=" + description + ", dateTime=" + dateTime
				+ ", transactionType=" + transactionType + ", amount=" + amount + "]";
	}

	
	
    
}
