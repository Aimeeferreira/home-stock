package com.home.stock.exception;

import java.util.List;

// Enum inválido
public record InvalidFieldError(
        int status,
        String message,
        String field,
        String rejectedValue,
        List<String> acceptedValues
) {
}