package com.bankapp.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankapp.api.request.AccountDetailUpdateRequest;
import com.bankapp.api.request.AccountRequest;
import com.bankapp.api.response.AccountResponse;
import com.bankapp.common.logging.Loggable;
import com.bankapp.entities.Account;
import com.bankapp.exceptions.BankAccountNotFoundException;
import com.bankapp.mapper.AccountMapper;
import com.bankapp.repo.AccountRepo;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountMapper accountMapper;
	private final AccountRepo accountRepo;

	@Override
	public List<AccountResponse> getAll() {
		return accountRepo.findAll().stream()
				.sorted(Comparator.comparingInt(Account::getId))
				.map(accountMapper::toResponse)
				.toList();
	}

	@Override
	public AccountResponse getById(int id) {
		Account account = accountRepo.findById(id).orElseThrow(() -> new RuntimeException("Account not found: " + id));

		return accountMapper.toResponse(account);
	}

	private Account getAccountEntity(int id) {
		return accountRepo.findById(id).orElseThrow(() -> new BankAccountNotFoundException("Account not found: " + id));
	}

	@Override
	public AccountResponse updateAccount(int id, AccountDetailUpdateRequest  accountDetailUpdateRequest) {
		Account accountToUpdate = getAccountEntity(id);
		accountToUpdate.setPhone(accountDetailUpdateRequest.getPhone());
		accountToUpdate.setEmail(accountDetailUpdateRequest.getEmail());
		accountRepo.save(accountToUpdate);

		return accountMapper.toResponse(accountToUpdate);
	}
   
    @PreAuthorize("""
        hasRole('MGR') 
        or (hasRole('CLERK') and #amount < 200)
    """)
	@Loggable
	@Override
	public void transfer(int fromAccId, int toAccId, BigDecimal amount) {
		Account fromAcc = getAccountEntity(fromAccId);
		Account toAcc = getAccountEntity(toAccId);

		fromAcc.setBalance(fromAcc.getBalance().subtract(amount));

		accountRepo.save(fromAcc);

		toAcc.setBalance(toAcc.getBalance().add(amount));

		accountRepo.save(toAcc);

	}

	@Override
	public void deposit(int accId, BigDecimal amount) {
		Account acc = getAccountEntity(accId);
		acc.setBalance(acc.getBalance().add(amount));
		accountRepo.save(acc);

	}

	@Override
	public void withdraw(int accId, BigDecimal amount) {
		Account acc = getAccountEntity(accId);
		acc.setBalance(acc.getBalance().subtract(amount));
		accountRepo.save(acc);
	}

	@Override
	public AccountResponse addAccount(AccountRequest request) {
		Account account = accountMapper.toEntity(request);
		accountRepo.save(account);
		return accountMapper.toResponse(account);
	}

	@Override
	public void deleteAccount(int id) {
		Account accountToDelete = getAccountEntity(id);
		accountRepo.delete(accountToDelete);
		accountRepo.compactIdsAfterDelete(id);
		accountRepo.resetAutoIncrement();
	}



}
