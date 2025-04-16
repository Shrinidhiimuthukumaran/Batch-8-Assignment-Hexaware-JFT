package com.hexaware.bankingsystem.dao;

import com.hexaware.bankingsystem.bean.Account;

public interface ICustomerServiceProviderdao {
    float getAccountBalance(long accNo);
    float deposit(long accNo, float amount);
    float withdraw(long accNo, float amount);
    void transfer(long fromAccNo, long toAccNo, float amount);
    Account getAccountDetails(long accNo);
}
