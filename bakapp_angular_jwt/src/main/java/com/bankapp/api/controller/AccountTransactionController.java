package com.bankapp.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.api.request.DepositRequest;
import com.bankapp.api.request.TransferRequest;
import com.bankapp.api.request.WithdrawRequest;
import com.bankapp.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "v1/transactions")
public class AccountTransactionController {

	private AccountService accountService;

	public AccountTransactionController(AccountService accountService) {
		this.accountService = accountService;
	}

	// transfer
	@PutMapping("transfer")
	public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest transferRequst) {
		accountService.transfer(transferRequst.fromAccountId(), transferRequst.toAccountId(), transferRequst.amount());
		return ResponseEntity.noContent().build();
	}

	@PutMapping("deposit")
	public ResponseEntity<Void> deposit(@RequestBody DepositRequest depositRequest) {
		accountService.deposit(depositRequest.accountId(), depositRequest.amount());
		return ResponseEntity.noContent().build();
	}

	@PutMapping("withdraw")
	public ResponseEntity<Void> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
		accountService.withdraw(withdrawRequest.accountId(), withdrawRequest.amount());
		return ResponseEntity.noContent().build();
	}

}
