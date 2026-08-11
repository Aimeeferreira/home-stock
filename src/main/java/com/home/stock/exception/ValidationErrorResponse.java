package com.home.stock.exception;

public record ValidationErrorResponse(
        String field,
        String message
) {
}