package com.bankapp.api.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
	private int id;
	private String name;
	private BigDecimal balance;
	private String email;
	private String phone;

}