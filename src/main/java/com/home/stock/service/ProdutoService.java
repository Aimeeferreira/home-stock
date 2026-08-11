package com.home.stock.service;

import com.home.stock.dto.request.ProdutoRequest;
import com.home.stock.dto.response.ProdutoResponse;
import com.home.stock.entity.Produto;
import com.home.stock.exception.ProdutoNotFoundException;
import com.home.stock.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    private ProdutoResponse toResponse(Produto produto) {

        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getUnidadeMedida(),
                produto.getEstoqueMinimo(),
                produto.getQuantidadeEstoque()
        );
    }

    public ProdutoResponse criar(ProdutoRequest request) {

        Produto produto = new Produto();

        produto.setNome(request.nome());
        produto.setCategoria(request.categoria());
        produto.setUnidadeMedida(request.unidadeMedida());
        produto.setEstoqueMinimo(request.estoqueMinimo());

        Produto produtoSalvo = produtoRepository.save(produto);

        return toResponse(produtoSalvo);
    }

    public List<ProdutoResponse> listar() {

        return produtoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProdutoResponse buscarPorId(Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));

        return toResponse(produto);
    }

    public ProdutoResponse atualizar(
            Long id,
            ProdutoRequest request
    ) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));

        produto.setNome(request.nome());
        produto.setCategoria(request.categoria());
        produto.setUnidadeMedida(request.unidadeMedida());
        produto.setEstoqueMinimo(request.estoqueMinimo());

        Produto produtoAtualizado = produtoRepository.save(produto);

        return toResponse(produtoAtualizado);
    }

    public void deletar(Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));

        produtoRepository.delete(produto);
    }
}