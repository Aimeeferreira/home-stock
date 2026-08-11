package com.home.stock.exception;

import java.util.List;

public record InvalidFieldError(
        int status,
        String message,
        String field,
        String rejectedValue,
        List<String> acceptedValues
) {
}