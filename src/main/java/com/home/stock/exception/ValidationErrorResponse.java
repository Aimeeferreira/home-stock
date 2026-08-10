package com.family.stock.exception;

public record ValidationErrorResponse(
        String field,
        String message
) {
}