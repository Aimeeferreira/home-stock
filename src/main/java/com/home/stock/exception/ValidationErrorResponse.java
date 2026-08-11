package com.home.stock.exception;

// Erros de Validação - @Valid
public record ValidationErrorResponse(
        String field,
        String message
) {
}