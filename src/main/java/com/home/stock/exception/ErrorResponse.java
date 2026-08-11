package com.home.stock.exception;

// Erros 404
public record ErrorResponse(
        int status,
        String message
) {
}