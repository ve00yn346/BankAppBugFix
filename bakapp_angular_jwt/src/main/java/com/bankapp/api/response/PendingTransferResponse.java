package com.bankapp.api.response;

import java.math.BigDecimal;

public record PendingTransferResponse(Long requestId, Integer fromAccountId, Integer toAccountId, BigDecimal amount,
		String initiatedBy) {
}
