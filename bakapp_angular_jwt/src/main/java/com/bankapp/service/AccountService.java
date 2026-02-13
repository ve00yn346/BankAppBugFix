package com.bankapp.service;

import java.math.BigDecimal;
import java.util.List;

import com.bankapp.api.request.AccountDetailUpdateRequest;
import com.bankapp.api.request.AccountRequest;
import com.bankapp.api.response.AccountResponse;
import com.bankapp.entities.Account;

public interface AccountService {
	
    List<AccountResponse> getAll();
    
    AccountResponse getById(int id);
    
    AccountResponse addAccount(AccountRequest request);
    
    public void deleteAccount(int id);
    
    public AccountResponse updateAccount(int id, AccountDetailUpdateRequest accountDetailUpdateRequest);

    public void transfer(int fromAccId, int toAccId, BigDecimal amount);
    
    public void deposit(int accId, BigDecimal amount);
    
    public void withdraw(int accId, BigDecimal amount);
}
