package com.home.stock.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome

) {
}