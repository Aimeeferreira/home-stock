package com.home.stock.service;

import com.home.stock.dto.request.ProdutoRequest;
import com.home.stock.dto.response.ProdutoResponse;
import com.home.stock.entity.Categoria;
import com.home.stock.entity.Produto;
import com.home.stock.entity.UnidadeMedida;
import com.home.stock.exception.CategoryNotFoundException;
import com.home.stock.exception.ProductNotFoundException;
import com.home.stock.repository.CategoriaRepository;
import com.home.stock.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Categoria categoria;
    private Produto produto;

    @BeforeEach
    void setUp() {

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Alimentação");

        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste: Arroz 5kg");
        produto.setCategoria(categoria);
        produto.setUnidadeMedida(UnidadeMedida.KG);
        produto.setEstoqueMinimo(BigDecimal.ONE);
        produto.setQuantidadeEstoque(BigDecimal.ZERO);
    }

    @Test
    void deveCriarProduto() {

        ProdutoRequest request = new ProdutoRequest(
                "Produto Teste: Arroz 5kg",
                "Alimentação",
                UnidadeMedida.KG,
                BigDecimal.ONE
        );

        when(categoriaRepository.findByNome("Alimentação"))
                .thenReturn(Optional.of(categoria));

        when(produtoRepository.save(any(Produto.class)))
                .thenReturn(produto);

        ProdutoResponse response = produtoService.criar(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Produto Teste: Arroz 5kg", response.nome());
        assertEquals("Alimentação", response.categoria());

        verify(categoriaRepository).findByNome("Alimentação");
        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    void naoDeveCriarProdutoQuandoCategoriaNaoExiste() {

        ProdutoRequest request = new ProdutoRequest(
                "Produto Teste: Arroz 5kg",
                "Inexistente",
                UnidadeMedida.KG,
                BigDecimal.ONE
        );

        when(categoriaRepository.findByNome("Inexistente"))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> produtoService.criar(request)
        );

        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void deveListarTodosOsProdutos() {

        when(produtoRepository.findAll())
                .thenReturn(List.of(produto));

        List<ProdutoResponse> response =
                produtoService.listar(null, null);

        assertEquals(1, response.size());
        assertEquals("Produto Teste: Arroz 5kg", response.get(0).nome());
    }

    @Test
    void deveFiltrarProdutosPorNome() {

        when(produtoRepository.findAll())
                .thenReturn(List.of(produto));

        List<ProdutoResponse> response =
                produtoService.listar("arroz", null);

        assertEquals(1, response.size());
        assertEquals("Produto Teste: Arroz 5kg", response.get(0).nome());
    }

    @Test
    void deveFiltrarProdutosPorCategoria() {

        when(produtoRepository.findAll())
                .thenReturn(List.of(produto));

        List<ProdutoResponse> response =
                produtoService.listar(null, "alimentação");

        assertEquals(1, response.size());
        assertEquals("Alimentação", response.get(0).categoria());
    }

    @Test
    void deveCombinarFiltrosDeNomeECategoria() {

        when(produtoRepository.findAll())
                .thenReturn(List.of(produto));

        List<ProdutoResponse> response =
                produtoService.listar(
                        "arroz",
                        "alimentação"
                );

        assertEquals(1, response.size());
        assertEquals("Produto Teste: Arroz 5kg", response.get(0).nome());
    }

    @Test
    void deveBuscarProdutoPorId() {

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        ProdutoResponse response =
                produtoService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Produto Teste: Arroz 5kg", response.nome());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() {

        when(produtoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> produtoService.buscarPorId(99L)
        );
    }

    @Test
    void deveAtualizarProduto() {

        ProdutoRequest request = new ProdutoRequest(
                "Produto Teste: Arroz Camil 5kg",
                "Alimentação",
                UnidadeMedida.KG,
                BigDecimal.valueOf(2)
        );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(categoriaRepository.findByNome("Alimentação"))
                .thenReturn(Optional.of(categoria));

        when(produtoRepository.save(any(Produto.class)))
                .thenReturn(produto);

        ProdutoResponse response =
                produtoService.atualizar(1L, request);

        assertEquals("Produto Teste: Arroz Camil 5kg", response.nome());
        assertEquals(BigDecimal.valueOf(2), response.estoqueMinimo());

        verify(produtoRepository).save(produto);
    }

    @Test
    void naoDeveAtualizarProdutoInexistente() {

        ProdutoRequest request = new ProdutoRequest(
                "Produto Teste: Arroz Camil 5kg",
                "Alimentação",
                UnidadeMedida.KG,
                BigDecimal.valueOf(2)
        );

        when(produtoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> produtoService.atualizar(99L, request)
        );

        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void deveDeletarProduto() {

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        produtoService.deletar(1L);

        verify(produtoRepository).delete(produto);
    }

    @Test
    void naoDeveDeletarProdutoInexistente() {

        when(produtoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> produtoService.deletar(99L)
        );

        verify(produtoRepository, never())
                .delete(any(Produto.class));
    }
}