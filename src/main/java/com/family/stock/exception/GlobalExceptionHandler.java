package com.family.stock.exception;

import com.family.stock.entity.CategoriaProduto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public java.util.List<ValidationErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public InvalidFieldError handleMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        String message = exception.getMessage();

        if (message != null && message.contains("CategoriaProduto")) {

            String rejectedValue = extractRejectedValue(message);

            return new InvalidFieldError(
                    400,
                    "Categoria inválida",
                    "categoria",
                    rejectedValue,
                    Arrays.stream(CategoriaProduto.values())
                            .map(Enum::name)
                            .toList()
            );
        }

        return new InvalidFieldError(
                400,
                "Dados da requisição inválidos",
                null,
                null,
                null
        );
    }

    private String extractRejectedValue(String message) {

        String prefix = "from String \"";

        int start = message.indexOf(prefix);

        if (start == -1) {
            return null;
        }

        start += prefix.length();

        int end = message.indexOf("\"", start);

        if (end == -1) {
            return null;
        }

        return message.substring(start, end);
    }

    @ExceptionHandler(ProdutoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProdutoNotFound(
            ProdutoNotFoundException exception
    ) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
}