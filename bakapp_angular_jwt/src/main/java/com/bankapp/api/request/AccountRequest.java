package com.bankapp.api.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
	private int id;
	@NotBlank(message = "Name is required")
	private String name;
	@NotNull(message = "Opening balance is required")
	@DecimalMin(value = "0.0", inclusive = true, message = "Opening balance cannot be negative")
	private BigDecimal balance;
	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email")
	private String email;
	@NotBlank(message = "Phone is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone must be exactly 10 digits")
	private String phone;

}
