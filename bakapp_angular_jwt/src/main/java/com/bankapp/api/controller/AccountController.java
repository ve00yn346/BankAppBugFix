package com.bankapp.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.api.request.AccountDetailUpdateRequest;
import com.bankapp.api.request.AccountRequest;
import com.bankapp.api.response.AccountResponse;
import com.bankapp.entities.Account;
import com.bankapp.service.AccountService;

@RestController
@RequestMapping(path = "v1/accounts")
public class AccountController {
	
	private AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping
	public List<AccountResponse> getAll() {
		return accountService.getAll();
	}
	
	@PostMapping
	public ResponseEntity<AccountResponse> addAccount(@RequestBody AccountRequest accountRequest) {
	    AccountResponse saved = accountService.addAccount(accountRequest);
	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(saved);
	}


	@GetMapping(path = "{id}")
	public AccountResponse getById(@PathVariable(name = "id") int id) {
		return accountService.getById(id);
	}
	
	@DeleteMapping(path = "{id}")
	public ResponseEntity<Void> deleteById(@PathVariable(name = "id") int id) {
		 accountService.deleteAccount(id);
		 return ResponseEntity.noContent().build();
	}
	
	@PutMapping(path = "{id}")
	public ResponseEntity<AccountResponse> updateAccount(@PathVariable(name = "id") int id, 
			@RequestBody AccountDetailUpdateRequest accountDetailUpdateRequest) {
	    AccountResponse updated = accountService.updateAccount(id, accountDetailUpdateRequest);
	    return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(updated);
	}
	
	
}
