package com.family.stock.exception;

public record ErrorResponse(
        int status,
        String message
) {
}