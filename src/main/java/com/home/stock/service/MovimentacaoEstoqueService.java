package com.family.stock.service;

import com.family.stock.dto.request.MovimentacaoEstoqueRequest;
import com.family.stock.dto.response.MovimentacaoEstoqueResponse;
import com.family.stock.entity.MovimentacaoEstoque;
import com.family.stock.entity.Produto;
import com.family.stock.entity.TipoMovimentacao;
import com.family.stock.exception.InsufficientStockException;
import com.family.stock.exception.ProdutoNotFoundException;
import com.family.stock.repository.MovimentacaoEstoqueRepository;
import com.family.stock.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimentacaoEstoqueService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public MovimentacaoEstoqueResponse entrada(
            Long produtoId,
            MovimentacaoEstoqueRequest request
    ) {

        Produto produto = buscarProduto(produtoId);

        produto.setQuantidadeEstoque(
                produto.getQuantidadeEstoque().add(request.quantidade())
        );

        produtoRepository.save(produto);

        MovimentacaoEstoque movimentacao = registrarMovimentacao(
                produto,
                TipoMovimentacao.ENTRADA,
                request.quantidade()
        );

        return toResponse(movimentacao);
    }

    public MovimentacaoEstoqueResponse saida(
            Long produtoId,
            MovimentacaoEstoqueRequest request
    ) {

        Produto produto = buscarProduto(produtoId);

        BigDecimal quantidadeAtual = produto.getQuantidadeEstoque();

        if (quantidadeAtual.compareTo(request.quantidade()) < 0) {
            throw new InsufficientStockException();
        }

        produto.setQuantidadeEstoque(
                quantidadeAtual.subtract(request.quantidade())
        );

        produtoRepository.save(produto);

        MovimentacaoEstoque movimentacao = registrarMovimentacao(
                produto,
                TipoMovimentacao.SAIDA,
                request.quantidade()
        );

        return toResponse(movimentacao);
    }

    private Produto buscarProduto(Long produtoId) {

        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNotFoundException(produtoId));
    }

    private MovimentacaoEstoque registrarMovimentacao(
            Produto produto,
            TipoMovimentacao tipo,
            BigDecimal quantidade
    ) {

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();

        movimentacao.setProduto(produto);
        movimentacao.setTipo(tipo);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setData(LocalDateTime.now());

        return movimentacaoEstoqueRepository.save(movimentacao);
    }

    private MovimentacaoEstoqueResponse toResponse(
            MovimentacaoEstoque movimentacao
    ) {

        return new MovimentacaoEstoqueResponse(
                movimentacao.getId(),
                movimentacao.getProduto().getId(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade(),
                movimentacao.getData()
        );
    }

    public List<MovimentacaoEstoqueResponse> listar(TipoMovimentacao tipo) {

        List<MovimentacaoEstoque> movimentacoes;

        if (tipo == null) {
            movimentacoes = movimentacaoEstoqueRepository.findAll();
        } else {
            movimentacoes = movimentacaoEstoqueRepository.findByTipo(tipo);
        }

        return movimentacoes.stream()
                .map(this::toResponse)
                .toList();
    }
}