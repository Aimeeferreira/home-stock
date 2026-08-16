package com.home.stock.controller;

import com.home.stock.dto.response.MovimentacaoEstoqueResponse;
import com.home.stock.entity.TipoMovimentacao;
import com.home.stock.service.MovimentacaoEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoqueResponse>> listar(
            @RequestParam(required = false) TipoMovimentacao tipo,
            @RequestParam(required = false) String produto,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) LocalDateTime inicio,
            @RequestParam(required = false) LocalDateTime fim
    ) {
        return ResponseEntity.ok(
                movimentacaoEstoqueService.listar(
                        tipo,
                        produto,
                        categoria,
                        inicio,
                        fim
                )
        );
    }
}