package com.home.stock.exception;

public record ErrorResponse(
        int status,
        String message
) {
}