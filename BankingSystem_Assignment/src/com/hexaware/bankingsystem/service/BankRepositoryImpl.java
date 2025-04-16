package com.hexaware.bankingsystem.service;

import java.util.Date;
import java.util.List;

import com.hexaware.bankingsystem.bean.*;
import com.hexaware.bankingsystem.dao.BankRepositoryImpldao;
import com.hexaware.bankingsystem.dao.IBankRepositorydao;
import com.hexaware.bankingsystem.exceptions.*;

public class BankRepositoryImpl implements IBankRepository {

    IBankRepositorydao dao = new BankRepositoryImpldao();

    @Override
    public void createAccount(Customer customer, long accNo, String accType, float balance) {
        dao.createAccount(customer, accNo, accType, balance);
    }

    @Override
    public List<Account> listAccounts() {
        return dao.listAccounts();
    }

    @Override
    public float getAccountBalance(long accNo) throws InvalidAccountException {
        return dao.getAccountBalance(accNo);
    }

    @Override
    public float deposit(long accNo, float amount) throws InvalidAccountException {
        return dao.deposit(accNo, amount);
    }

    @Override
    public float withdraw(long accNo, float amount)
            throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException {
        return dao.withdraw(accNo, amount);
    }

    @Override
    public void transfer(long fromAccNo, long toAccNo, float amount)
            throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException {
        dao.transfer(fromAccNo, toAccNo, amount);
    }

    @Override
    public Account getAccountDetails(long accNo) throws InvalidAccountException {
        return dao.getAccountDetails(accNo);
    }

    @Override
    public List<Transaction> getTransactions(long accNo, Date fromDate, Date toDate) {
        return dao.getTransactions(accNo, fromDate, toDate);
    }

    @Override
    public void calculateInterest() {
        dao.calculateInterest();
    }
    
    @Override
    public boolean isCustomerExists(int customerId) {
        return dao.isCustomerExists(customerId);
    }

}
