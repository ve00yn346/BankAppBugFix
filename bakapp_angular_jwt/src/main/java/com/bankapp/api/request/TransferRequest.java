package com.bankapp.api.request;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferRequest(

		  @NotNull(message = "{transferRequest.fromAccountId.absent}")
	      @Positive(message = "{transferRequest.fromAccountId.positive}")
	       Integer fromAccountId,

	      @NotNull(message = "{transferRequest.toAccountId.absent}")
	      @Positive(message = "{transferRequest.toAccountId.positive}")
	      Integer toAccountId,

	      @NotNull(message = "{transferRequest.amount.absent}")
	      @Positive(message = "{transferRequest.amount.positive}")
	      BigDecimal amount
) {}