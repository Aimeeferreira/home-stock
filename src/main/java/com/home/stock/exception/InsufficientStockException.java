package com.home.stock.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException() {
        super("Estoque insuficiente para realizar a saída.");
    }
}