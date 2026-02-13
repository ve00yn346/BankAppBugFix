package com.bankapp;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bankapp.entities.Account;
import com.bankapp.repo.AccountRepo;
@Component
public class DbInit implements CommandLineRunner{

	@Autowired
	private AccountRepo accountRepo;
	
	@Override
	public void run(String... args) throws Exception {
//		accountRepo.save(new Account("amit", BigDecimal.valueOf(1000),"amit@gmail.com","6666333322"));
//		accountRepo.save(new Account("sumit", BigDecimal.valueOf(1000),"sumit@gmail.com","9966333322"));
//		
	}

}
