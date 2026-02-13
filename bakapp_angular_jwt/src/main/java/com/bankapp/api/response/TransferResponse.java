package com.bankapp.api.response;

public record TransferResponse(String status, String message, Long requestId) {
}
