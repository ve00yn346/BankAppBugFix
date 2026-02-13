package com.bankapp.api.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}