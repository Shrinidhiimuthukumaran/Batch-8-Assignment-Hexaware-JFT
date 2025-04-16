package com.hexaware.bankingsystem.service;

import java.util.Date;
import java.util.List;

import com.hexaware.bankingsystem.bean.*;

import com.hexaware.bankingsystem.exceptions.*;

public interface IBankRepository {
	
	void createAccount(Customer customer, long accNo, String accType, float balance);

    List<Account> listAccounts();

    float getAccountBalance(long accNo) throws InvalidAccountException;

    float deposit(long accNo, float amount) throws InvalidAccountException;

    float withdraw(long accNo, float amount)
        throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException;

    void transfer(long fromAccNo, long toAccNo, float amount)
        throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException;

    Account getAccountDetails(long accNo) throws InvalidAccountException;

    List<Transaction> getTransactions(long accNo, Date fromDate, Date toDate);

    void calculateInterest();
    
    boolean isCustomerExists(int customerId);

}
