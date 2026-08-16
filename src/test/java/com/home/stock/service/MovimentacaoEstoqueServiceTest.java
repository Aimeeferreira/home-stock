package com.home.stock.service;

import com.home.stock.dto.request.MovimentacaoEstoqueRequest;
import com.home.stock.dto.response.MovimentacaoEstoqueResponse;
import com.home.stock.entity.Categoria;
import com.home.stock.entity.MovimentacaoEstoque;
import com.home.stock.entity.Produto;
import com.home.stock.entity.TipoMovimentacao;
import com.home.stock.entity.UnidadeMedida;
import com.home.stock.exception.InsufficientStockException;
import com.home.stock.exception.ProductNotFoundException;
import com.home.stock.repository.MovimentacaoEstoqueRepository;
import com.home.stock.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimentacaoEstoqueServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @InjectMocks
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    private Categoria categoria;
    private Produto produto;
    private MovimentacaoEstoque movimentacao;

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
        produto.setQuantidadeEstoque(BigDecimal.TEN);

        movimentacao = new MovimentacaoEstoque();
        movimentacao.setId(1L);
        movimentacao.setProduto(produto);
        movimentacao.setTipo(TipoMovimentacao.ENTRADA);
        movimentacao.setQuantidade(BigDecimal.valueOf(5));
        movimentacao.setData(LocalDateTime.of(
                2026, 8, 10, 10, 0
        ));
    }

    @Test
    void deveRegistrarEntradaEAtualizarEstoque() {

        MovimentacaoEstoqueRequest request =
                new MovimentacaoEstoqueRequest(
                        BigDecimal.valueOf(5)
                );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(movimentacaoEstoqueRepository.save(
                any(MovimentacaoEstoque.class)
        )).thenReturn(movimentacao);

        MovimentacaoEstoqueResponse response =
                movimentacaoEstoqueService.entrada(1L, request);

        assertEquals(
                BigDecimal.valueOf(15),
                produto.getQuantidadeEstoque()
        );

        assertNotNull(response);

        verify(produtoRepository).save(produto);
        verify(movimentacaoEstoqueRepository)
                .save(any(MovimentacaoEstoque.class));
    }

    @Test
    void deveRegistrarSaidaEAtualizarEstoque() {

        MovimentacaoEstoqueRequest request =
                new MovimentacaoEstoqueRequest(
                        BigDecimal.valueOf(3)
                );

        movimentacao.setTipo(TipoMovimentacao.SAIDA);
        movimentacao.setQuantidade(BigDecimal.valueOf(3));

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(movimentacaoEstoqueRepository.save(
                any(MovimentacaoEstoque.class)
        )).thenReturn(movimentacao);

        MovimentacaoEstoqueResponse response =
                movimentacaoEstoqueService.saida(1L, request);

        assertEquals(
                BigDecimal.valueOf(7),
                produto.getQuantidadeEstoque()
        );

        assertNotNull(response);

        verify(produtoRepository).save(produto);
        verify(movimentacaoEstoqueRepository)
                .save(any(MovimentacaoEstoque.class));
    }

    @Test
    void naoDeveRegistrarSaidaQuandoEstoqueForInsuficiente() {

        MovimentacaoEstoqueRequest request =
                new MovimentacaoEstoqueRequest(
                        BigDecimal.valueOf(11)
                );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        assertThrows(
                InsufficientStockException.class,
                () -> movimentacaoEstoqueService.saida(1L, request)
        );

        assertEquals(
                BigDecimal.TEN,
                produto.getQuantidadeEstoque()
        );

        verify(produtoRepository, never())
                .save(any(Produto.class));

        verify(movimentacaoEstoqueRepository, never())
                .save(any(MovimentacaoEstoque.class));
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExisteNaEntrada() {

        MovimentacaoEstoqueRequest request =
                new MovimentacaoEstoqueRequest(
                        BigDecimal.valueOf(5)
                );

        when(produtoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> movimentacaoEstoqueService.entrada(99L, request)
        );

        verify(produtoRepository, never())
                .save(any(Produto.class));

        verify(movimentacaoEstoqueRepository, never())
                .save(any(MovimentacaoEstoque.class));
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExisteNaSaida() {

        MovimentacaoEstoqueRequest request =
                new MovimentacaoEstoqueRequest(
                        BigDecimal.valueOf(5)
                );

        when(produtoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> movimentacaoEstoqueService.saida(99L, request)
        );

        verify(produtoRepository, never())
                .save(any(Produto.class));

        verify(movimentacaoEstoqueRepository, never())
                .save(any(MovimentacaoEstoque.class));
    }

    @Test
    void deveRegistrarMovimentacaoDeEntradaComDadosCorretos() {

        MovimentacaoEstoqueRequest request =
                new MovimentacaoEstoqueRequest(
                        BigDecimal.valueOf(5)
                );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(movimentacaoEstoqueRepository.save(
                any(MovimentacaoEstoque.class)
        )).thenReturn(movimentacao);

        movimentacaoEstoqueService.entrada(1L, request);

        ArgumentCaptor<MovimentacaoEstoque> captor =
                ArgumentCaptor.forClass(MovimentacaoEstoque.class);

        verify(movimentacaoEstoqueRepository)
                .save(captor.capture());

        MovimentacaoEstoque registrada =
                captor.getValue();

        assertEquals(produto, registrada.getProduto());
        assertEquals(
                TipoMovimentacao.ENTRADA,
                registrada.getTipo()
        );
        assertEquals(
                BigDecimal.valueOf(5),
                registrada.getQuantidade()
        );
        assertNotNull(registrada.getData());
    }

    @Test
    void deveListarTodasAsMovimentacoes() {

        when(movimentacaoEstoqueRepository.findAll())
                .thenReturn(List.of(movimentacao));

        List<MovimentacaoEstoqueResponse> response =
                movimentacaoEstoqueService.listar(
                        null,
                        null,
                        null,
                        null,
                        null
                );

        assertEquals(1, response.size());
        assertEquals(
                TipoMovimentacao.ENTRADA,
                response.get(0).tipo()
        );
    }

    @Test
    void deveFiltrarPorTipo() {

        when(movimentacaoEstoqueRepository.findAll())
                .thenReturn(List.of(
                        movimentacao,
                        criarMovimentacaoSaida()
                ));

        List<MovimentacaoEstoqueResponse> response =
                movimentacaoEstoqueService.listar(
                        TipoMovimentacao.SAIDA,
                        null,
                        null,
                        null,
                        null
                );

        assertEquals(1, response.size());
        assertEquals(
                TipoMovimentacao.SAIDA,
                response.get(0).tipo()
        );
    }

    @Test
    void deveFiltrarPorProduto() {

        when(movimentacaoEstoqueRepository.findAll())
                .thenReturn(List.of(movimentacao));

        List<MovimentacaoEstoqueResponse> response =
                movimentacaoEstoqueService.listar(
                        null,
                        "arroz",
                        null,
                        null,
                        null
                );

        assertEquals(1, response.size());
    }

    @Test
    void deveFiltrarPorCategoria() {

        when(movimentacaoEstoqueRepository.findAll())
                .thenReturn(List.of(movimentacao));

        List<MovimentacaoEstoqueResponse> response =
                movimentacaoEstoqueService.listar(
                        null,
                        null,
                        "alimentação",
                        null,
                        null
                );

        assertEquals(1, response.size());
    }

    @Test
    void deveFiltrarPorPeriodo() {

        when(movimentacaoEstoqueRepository.findAll())
                .thenReturn(List.of(movimentacao));

        LocalDateTime inicio =
                LocalDateTime.of(2026, 8, 1, 0, 0);

        LocalDateTime fim =
                LocalDateTime.of(2026, 8, 31, 23, 59);

        List<MovimentacaoEstoqueResponse> response =
                movimentacaoEstoqueService.listar(
                        null,
                        null,
                        null,
                        inicio,
                        fim
                );

        assertEquals(1, response.size());
    }

    @Test
    void deveCombinarFiltros() {

        when(movimentacaoEstoqueRepository.findAll())
                .thenReturn(List.of(
                        movimentacao,
                        criarMovimentacaoSaida()
                ));

        LocalDateTime inicio =
                LocalDateTime.of(2026, 8, 1, 0, 0);

        LocalDateTime fim =
                LocalDateTime.of(2026, 8, 31, 23, 59);

        List<MovimentacaoEstoqueResponse> response =
                movimentacaoEstoqueService.listar(
                        TipoMovimentacao.ENTRADA,
                        "arroz",
                        "alimentação",
                        inicio,
                        fim
                );

        assertEquals(1, response.size());

        assertEquals(
                TipoMovimentacao.ENTRADA,
                response.get(0).tipo()
        );
    }

    private MovimentacaoEstoque criarMovimentacaoSaida() {

        MovimentacaoEstoque saida =
                new MovimentacaoEstoque();

        saida.setId(2L);
        saida.setProduto(produto);
        saida.setTipo(TipoMovimentacao.SAIDA);
        saida.setQuantidade(BigDecimal.valueOf(2));
        saida.setData(
                LocalDateTime.of(2026, 8, 12, 15, 0)
        );

        return saida;
    }
}