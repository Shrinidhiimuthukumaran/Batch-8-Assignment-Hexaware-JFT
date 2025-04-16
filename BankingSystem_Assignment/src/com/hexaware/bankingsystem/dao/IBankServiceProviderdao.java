package com.hexaware.bankingsystem.dao;

import com.hexaware.bankingsystem.bean.Account;
import com.hexaware.bankingsystem.bean.Customer;

public interface IBankServiceProviderdao {
	
	void createAccount(Customer customer, long accNo, String accType, float balance);
    Account[] listAccounts();
    void calculateInterest();
    Account getAccountDetails(long accNo);

}
