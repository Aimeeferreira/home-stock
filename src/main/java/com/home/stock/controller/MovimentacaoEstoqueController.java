package com.family.stock.controller;

import com.family.stock.dto.response.MovimentacaoEstoqueResponse;
import com.family.stock.entity.TipoMovimentacao;
import com.family.stock.service.MovimentacaoEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoqueResponse>> listar(
            @RequestParam(required = false) TipoMovimentacao tipo
    ) {
        return ResponseEntity.ok(
                movimentacaoEstoqueService.listar(tipo)
        );
    }
}