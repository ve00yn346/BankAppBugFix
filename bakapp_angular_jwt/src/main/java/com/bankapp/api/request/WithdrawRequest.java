package com.bankapp.api.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public record WithdrawRequest ( Integer accountId,BigDecimal amount) {}
