package com.home.stock.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("Categoria não encontrada com o id: " + id);
    }

    public CategoryNotFoundException(String nome) {
        super("Categoria não encontrada: " + nome);
    }
}