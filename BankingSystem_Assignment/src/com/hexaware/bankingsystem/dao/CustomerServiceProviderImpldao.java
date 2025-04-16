package com.hexaware.bankingsystem.dao;

import com.hexaware.bankingsystem.bean.Account;
import com.hexaware.bankingsystem.exceptions.*;

public class CustomerServiceProviderImpldao implements ICustomerServiceProviderdao {

    private IBankRepositorydao bankRepository = new BankRepositoryImpldao();

    @Override
    public float getAccountBalance(long accNo) {
        try {
            return bankRepository.getAccountBalance(accNo);
        } catch (InvalidAccountException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public float deposit(long accNo, float amount) {
        try {
            return bankRepository.deposit(accNo, amount);
        } catch (InvalidAccountException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public float withdraw(long accNo, float amount) {
        try {
            return bankRepository.withdraw(accNo, amount);
        } catch (InvalidAccountException | InsufficientFundException | OverDraftLimitExceededException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public void transfer(long fromAccNo, long toAccNo, float amount) {
        try {
            bankRepository.transfer(fromAccNo, toAccNo, amount);
        } catch (InvalidAccountException | InsufficientFundException | OverDraftLimitExceededException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Account getAccountDetails(long accNo) {
        try {
            return bankRepository.getAccountDetails(accNo);
        } catch (InvalidAccountException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
