package com.bankapp.mapper;

import org.mapstruct.Mapper;

import com.bankapp.api.request.AccountRequest;
import com.bankapp.api.response.AccountResponse;
import com.bankapp.entities.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toResponse(Account account);
    Account toEntity(AccountRequest request);
}