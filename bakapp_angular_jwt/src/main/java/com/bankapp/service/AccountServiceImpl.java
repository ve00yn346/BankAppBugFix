package com.bankapp.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankapp.api.request.AccountDetailUpdateRequest;
import com.bankapp.api.request.AccountRequest;
import com.bankapp.api.response.AccountResponse;
import com.bankapp.api.response.PendingTransferResponse;
import com.bankapp.api.response.TransferResponse;
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
	private final AtomicLong pendingTransferSequence = new AtomicLong(0);
	private final Map<Long, PendingTransfer> pendingTransfers = new ConcurrentHashMap<>();

	private static final BigDecimal MANAGER_APPROVAL_THRESHOLD = BigDecimal.valueOf(200);

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
   
	@PreAuthorize("hasAnyRole('CLERK', 'MGR')")
	@Loggable
	@Override
	public TransferResponse transfer(int fromAccId, int toAccId, BigDecimal amount) {
		validateTransferInputs(fromAccId, toAccId, amount);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean clerkLoggedIn = authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_CLERK"));

		if (clerkLoggedIn && amount.compareTo(MANAGER_APPROVAL_THRESHOLD) >= 0) {
			long requestId = pendingTransferSequence.incrementAndGet();
			pendingTransfers.put(requestId,
					new PendingTransfer(requestId, fromAccId, toAccId, amount, authentication.getName()));
			return new TransferResponse("PENDING_APPROVAL",
					"Transfer initiated. Manager approval is required for amount >= 200.", requestId);
		}

		executeTransfer(fromAccId, toAccId, amount);
		return new TransferResponse("COMPLETED", "Transfer completed successfully.", null);
	}

	@Override
	@PreAuthorize("hasRole('MGR')")
	public List<PendingTransferResponse> getPendingTransfers() {
		return pendingTransfers.values().stream()
				.sorted(Comparator.comparingLong(PendingTransfer::requestId))
				.map(p -> new PendingTransferResponse(p.requestId(), p.fromAccId(), p.toAccId(), p.amount(), p.initiatedBy()))
				.toList();
	}

	@Override
	@PreAuthorize("hasRole('MGR')")
	public void approveTransfer(long requestId) {
		PendingTransfer pendingTransfer = pendingTransfers.remove(requestId);
		if (pendingTransfer == null) {
			throw new IllegalArgumentException("Pending transfer request not found: " + requestId);
		}

		executeTransfer(pendingTransfer.fromAccId(), pendingTransfer.toAccId(), pendingTransfer.amount());
	}

	private void executeTransfer(int fromAccId, int toAccId, BigDecimal amount) {
		Account fromAcc = getAccountEntity(fromAccId);
		Account toAcc = getAccountEntity(toAccId);
		if (fromAcc.getBalance().compareTo(amount) < 0) {
			throw new IllegalArgumentException("Insufficient balance in source account");
		}

		fromAcc.setBalance(fromAcc.getBalance().subtract(amount));

		accountRepo.save(fromAcc);

		toAcc.setBalance(toAcc.getBalance().add(amount));

		accountRepo.save(toAcc);
	}

	private void validateTransferInputs(int fromAccId, int toAccId, BigDecimal amount) {
		if (fromAccId == toAccId) {
			throw new IllegalArgumentException("From account and To account cannot be same");
		}
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Transfer amount must be greater than 0");
		}
	}

	@Override
	public void deposit(int accId, BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Deposit amount must be greater than 0");
		}
		Account acc = getAccountEntity(accId);
		acc.setBalance(acc.getBalance().add(amount));
		accountRepo.save(acc);

	}

	@Override
	public void withdraw(int accId, BigDecimal amount) {
		Account acc = getAccountEntity(accId);
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Withdrawal amount must be greater than 0");
		}
		if (acc.getBalance().compareTo(amount) < 0) {
			throw new IllegalArgumentException("Insufficient balance in account");
		}
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


	record PendingTransfer(Long requestId, int fromAccId, int toAccId, BigDecimal amount, String initiatedBy) {
	}


}
