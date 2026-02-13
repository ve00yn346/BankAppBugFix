package com.bankapp.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.api.request.DepositRequest;
import com.bankapp.api.request.TransferRequest;
import com.bankapp.api.request.WithdrawRequest;
import com.bankapp.api.response.PendingTransferResponse;
import com.bankapp.api.response.TransferResponse;
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
	public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest transferRequst) {
		TransferResponse response = accountService.transfer(transferRequst.fromAccountId(), transferRequst.toAccountId(),
				transferRequst.amount());
		if ("PENDING_APPROVAL".equals(response.status())) {
			return ResponseEntity.accepted().body(response);
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("transfer/pending")
	public ResponseEntity<java.util.List<PendingTransferResponse>> getPendingTransfers() {
		return ResponseEntity.ok(accountService.getPendingTransfers());
	}

	@PutMapping("transfer/approve/{requestId}")
	public ResponseEntity<Void> approveTransfer(@PathVariable long requestId) {
		accountService.approveTransfer(requestId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("deposit")
	public ResponseEntity<Void> deposit(@Valid @RequestBody DepositRequest depositRequest) {
		accountService.deposit(depositRequest.accountId(), depositRequest.amount());
		return ResponseEntity.noContent().build();
	}

	@PutMapping("withdraw")
	public ResponseEntity<Void> withdraw(@Valid @RequestBody WithdrawRequest withdrawRequest) {
		accountService.withdraw(withdrawRequest.accountId(), withdrawRequest.amount());
		return ResponseEntity.noContent().build();
	}

}
