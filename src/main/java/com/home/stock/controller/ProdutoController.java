package com.home.stock.controller;

import com.home.stock.dto.request.MovimentacaoEstoqueRequest;
import com.home.stock.dto.request.ProdutoRequest;
import com.home.stock.dto.response.MovimentacaoEstoqueResponse;
import com.home.stock.dto.response.ProdutoResponse;
import com.home.stock.service.MovimentacaoEstoqueService;
import com.home.stock.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;
    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(
            @RequestBody @Valid ProdutoRequest request
    ) {
        ProdutoResponse response = produtoService.criar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                produtoService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProdutoRequest request
    ) {
        return ResponseEntity.ok(
                produtoService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {
        produtoService.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/entradas")
    public ResponseEntity<MovimentacaoEstoqueResponse> entrada(
            @PathVariable Long id,
            @RequestBody @Valid MovimentacaoEstoqueRequest request
    ) {
        MovimentacaoEstoqueResponse response =
                movimentacaoEstoqueService.entrada(id, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{id}/saidas")
    public ResponseEntity<MovimentacaoEstoqueResponse> saida(
            @PathVariable Long id,
            @RequestBody @Valid MovimentacaoEstoqueRequest request
    ) {
        MovimentacaoEstoqueResponse response =
                movimentacaoEstoqueService.saida(id, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}