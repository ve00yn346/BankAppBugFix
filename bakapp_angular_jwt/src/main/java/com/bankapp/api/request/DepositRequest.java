package com.bankapp.api.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DepositRequest(
		@NotNull(message = "{transaction.accountId.absent}")
		@Positive(message = "{transaction.accountId.positive}")
		Integer accountId,
		@NotNull(message = "{transaction.amount.absent}")
		@Positive(message = "{transaction.amount.positive}")
		BigDecimal amount
) {}
