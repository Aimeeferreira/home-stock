package com.home.stock.service;

import com.home.stock.dto.request.ProdutoRequest;
import com.home.stock.dto.response.CategoriaResponse;
import com.home.stock.dto.response.ProdutoResponse;
import com.home.stock.entity.Categoria;
import com.home.stock.entity.Produto;
import com.home.stock.exception.CategoryNotFoundException;
import com.home.stock.exception.ProductNotFoundException;
import com.home.stock.repository.CategoriaRepository;
import com.home.stock.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    private Categoria buscarCategoria(String nome) {

        return categoriaRepository.findByNome(nome)
                .orElseThrow(() -> new CategoryNotFoundException(nome));
    }

    private ProdutoResponse toResponse(Produto produto) {

        CategoriaResponse categoriaResponse = new CategoriaResponse(
                produto.getCategoria().getId(),
                produto.getCategoria().getNome()
        );

        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria().getNome(),
                produto.getUnidadeMedida(),
                produto.getEstoqueMinimo(),
                produto.getQuantidadeEstoque()
        );
    }

    public ProdutoResponse criar(ProdutoRequest request) {

        Categoria categoria = buscarCategoria(request.categoria());

        Produto produto = new Produto();

        produto.setNome(request.nome());
        produto.setCategoria(categoria);
        produto.setUnidadeMedida(request.unidadeMedida());
        produto.setEstoqueMinimo(request.estoqueMinimo());

        Produto produtoSalvo = produtoRepository.save(produto);

        return toResponse(produtoSalvo);
    }

    public List<ProdutoResponse> listar(
            String nome,
            String categoria
    ) {

        List<Produto> produtos = produtoRepository.findAll();

        return produtos.stream()
                .filter(produto ->
                        nome == null ||
                                nome.isBlank() ||
                                produto.getNome()
                                        .toLowerCase()
                                        .contains(nome.toLowerCase())
                )
                .filter(produto ->
                        categoria == null ||
                                categoria.isBlank() ||
                                produto.getCategoria()
                                        .getNome()
                                        .equalsIgnoreCase(categoria)
                )
                .map(this::toResponse)
                .toList();
    }

    public ProdutoResponse buscarPorId(Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return toResponse(produto);
    }

    public ProdutoResponse atualizar(
            Long id,
            ProdutoRequest request
    ) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Categoria categoria = buscarCategoria(request.categoria());

        produto.setNome(request.nome());
        produto.setCategoria(categoria);
        produto.setUnidadeMedida(request.unidadeMedida());
        produto.setEstoqueMinimo(request.estoqueMinimo());

        Produto produtoAtualizado = produtoRepository.save(produto);

        return toResponse(produtoAtualizado);
    }

    public void deletar(Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        produtoRepository.delete(produto);
    }
}