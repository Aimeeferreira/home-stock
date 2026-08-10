package com.family.stock.dto.response;

import com.family.stock.entity.TipoMovimentacao;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoEstoqueResponse(
        Long id,
        Long produtoId,
        TipoMovimentacao tipo,
        BigDecimal quantidade,

        @JsonFormat(pattern = "HH'h'mm dd/MM/yyyy")
        LocalDateTime data
) {
}