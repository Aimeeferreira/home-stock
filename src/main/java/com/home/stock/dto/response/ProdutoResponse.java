package com.home.stock.dto.response;

import com.home.stock.entity.CategoriaProduto;
import com.home.stock.entity.UnidadeMedida;
import java.math.BigDecimal;

// Record representa um objeto imutável para transporte de dados
public record ProdutoResponse(
        Long id,
        String nome,
        CategoriaProduto categoria,
        UnidadeMedida unidadeMedida,
        BigDecimal estoqueMinimo,
        BigDecimal quantidadeEstoque
) {
}