package com.home.stock.controller;

import com.home.stock.dto.request.MovimentacaoEstoqueRequest;
import com.home.stock.dto.response.MovimentacaoEstoqueResponse;
import com.home.stock.dto.response.ProdutoResponse;
import com.home.stock.entity.TipoMovimentacao;
import com.home.stock.entity.UnidadeMedida;
import com.home.stock.exception.InsufficientStockException;
import com.home.stock.exception.ProductNotFoundException;
import com.home.stock.service.MovimentacaoEstoqueService;
import com.home.stock.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @MockitoBean
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Test
    void deveCriarProduto() throws Exception {

        ProdutoResponse response = new ProdutoResponse(
                1L,
                "Produto Teste",
                "Alimentação",
                UnidadeMedida.KG,
                BigDecimal.ONE,
                BigDecimal.ZERO
        );

        when(produtoService.criar(any()))
                .thenReturn(response);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nome": "Produto Teste",
                                    "categoria": "Alimentação",
                                    "unidadeMedida": "KG",
                                    "estoqueMinimo": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.categoria").value("Alimentação"));

        verify(produtoService).criar(any());
    }

    @Test
    void deveListarProdutos() throws Exception {

        ProdutoResponse response = new ProdutoResponse(
                1L,
                "Produto Teste",
                "Alimentação",
                UnidadeMedida.KG,
                BigDecimal.ONE,
                BigDecimal.ZERO
        );

        when(produtoService.listar(null, null))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Produto Teste"))
                .andExpect(jsonPath("$[0].categoria").value("Alimentação"));

        verify(produtoService).listar(null, null);
    }

    @Test
    void deveListarProdutosComFiltros() throws Exception {

        when(produtoService.listar(
                "arroz",
                "Alimentação"
        )).thenReturn(List.of());

        mockMvc.perform(get("/produtos")
                        .param("nome", "arroz")
                        .param("categoria", "Alimentação"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(produtoService)
                .listar("arroz", "Alimentação");
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {

        ProdutoResponse response = new ProdutoResponse(
                1L,
                "Produto Teste",
                "Alimentação",
                UnidadeMedida.KG,
                BigDecimal.ONE,
                BigDecimal.ZERO
        );

        when(produtoService.buscarPorId(1L))
                .thenReturn(response);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.categoria").value("Alimentação"));

        verify(produtoService).buscarPorId(1L);
    }

    @Test
    void deveAtualizarProduto() throws Exception {

        ProdutoResponse response = new ProdutoResponse(
                1L,
                "Produto Atualizado",
                "Alimentação",
                UnidadeMedida.KG,
                BigDecimal.valueOf(2),
                BigDecimal.ZERO
        );

        when(produtoService.atualizar(
                eq(1L),
                any()
        )).thenReturn(response);

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nome": "Produto Atualizado",
                                    "categoria": "Alimentação",
                                    "unidadeMedida": "KG",
                                    "estoqueMinimo": 2
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Atualizado"))
                .andExpect(jsonPath("$.categoria").value("Alimentação"));

        verify(produtoService)
                .atualizar(eq(1L), any());
    }

    @Test
    void deveDeletarProduto() throws Exception {

        doNothing()
                .when(produtoService)
                .deletar(1L);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());

        verify(produtoService).deletar(1L);
    }

    @Test
    void deveRegistrarEntrada() throws Exception {

        MovimentacaoEstoqueResponse response =
                new MovimentacaoEstoqueResponse(
                        1L,
                        1L,
                        TipoMovimentacao.ENTRADA,
                        BigDecimal.valueOf(5),
                        LocalDateTime.of(
                                2026, 8, 16, 12, 0
                        )
                );

        when(movimentacaoEstoqueService.entrada(
                eq(1L),
                any(MovimentacaoEstoqueRequest.class)
        )).thenReturn(response);

        mockMvc.perform(post("/produtos/1/entradas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "quantidade": 5
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.produtoId").value(1))
                .andExpect(jsonPath("$.tipo").value("ENTRADA"))
                .andExpect(jsonPath("$.quantidade").value(5));

        verify(movimentacaoEstoqueService)
                .entrada(
                        eq(1L),
                        any(MovimentacaoEstoqueRequest.class)
                );
    }

    @Test
    void deveRegistrarSaida() throws Exception {

        MovimentacaoEstoqueResponse response =
                new MovimentacaoEstoqueResponse(
                        1L,
                        1L,
                        TipoMovimentacao.SAIDA,
                        BigDecimal.valueOf(3),
                        LocalDateTime.of(
                                2026, 8, 16, 12, 0
                        )
                );

        when(movimentacaoEstoqueService.saida(
                eq(1L),
                any(MovimentacaoEstoqueRequest.class)
        )).thenReturn(response);

        mockMvc.perform(post("/produtos/1/saidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "quantidade": 3
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.produtoId").value(1))
                .andExpect(jsonPath("$.tipo").value("SAIDA"))
                .andExpect(jsonPath("$.quantidade").value(3));

        verify(movimentacaoEstoqueService)
                .saida(
                        eq(1L),
                        any(MovimentacaoEstoqueRequest.class)
                );
    }

    @Test
    void deveRetornar404QuandoProdutoNaoExiste() throws Exception {

        when(produtoService.buscarPorId(99L))
                .thenThrow(new ProductNotFoundException(99L));

        mockMvc.perform(get("/produtos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(produtoService).buscarPorId(99L);
    }

    // Exceções
    @Test
    void deveRetornarErroQuandoNomeDoProdutoNaoForInformado() throws Exception {

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "nome": "",
                                "categoria": "Alimentação",
                                "unidadeMedida": "KG",
                                "estoqueMinimo": 1
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("nome"))
                .andExpect(jsonPath("$[0].message")
                        .value("Nome é obrigatório"));

        verifyNoInteractions(produtoService);
    }

    @Test
    void deveRetornarErroQuandoCategoriaNaoForInformada() throws Exception {

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "nome": "Produto Teste",
                                "unidadeMedida": "KG",
                                "estoqueMinimo": 1
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("categoria"))
                .andExpect(jsonPath("$[0].message")
                        .value("Categoria é obrigatória"));

        verifyNoInteractions(produtoService);
    }

    @Test
    void deveRetornarErroQuandoEstoqueMinimoForNegativo() throws Exception {

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "nome": "Produto Teste",
                                "categoria": "Alimentação",
                                "unidadeMedida": "KG",
                                "estoqueMinimo": -1
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field")
                        .value("estoqueMinimo"))
                .andExpect(jsonPath("$[0].message")
                        .value("Estoque mínimo não pode ser negativo"));

        verifyNoInteractions(produtoService);
    }

    @Test
    void deveRetornarErroQuandoUnidadeDeMedidaNaoForInformada() throws Exception {

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "nome": "Produto Teste",
                                "categoria": "Alimentação",
                                "estoqueMinimo": 1
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field")
                        .value("unidadeMedida"))
                .andExpect(jsonPath("$[0].message")
                        .value("Unidade de medida é obrigatória"));

        verifyNoInteractions(produtoService);
    }

    @Test
    void deveRetornarErroQuandoNomeDoProdutoExcederLimite() throws Exception {

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "nome": "Este é um nome de produto que possui mais de cinquenta caracteres para teste",
                                "categoria": "Alimentação",
                                "unidadeMedida": "KG",
                                "estoqueMinimo": 1
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field")
                        .value("nome"))
                .andExpect(jsonPath("$[0].message")
                        .value("Nome deve ter no máximo 50 caracteres"));

        verifyNoInteractions(produtoService);
    }

    @Test
    void deveRetornarErroQuandoQuantidadeNaoForInformada() throws Exception {

        mockMvc.perform(post("/produtos/1/entradas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field")
                        .value("quantidade"))
                .andExpect(jsonPath("$[0].message")
                        .value("Quantidade é obrigatória"));

        verifyNoInteractions(movimentacaoEstoqueService);
    }

    @Test
    void deveRetornarErroQuandoQuantidadeForZero() throws Exception {

        mockMvc.perform(post("/produtos/1/entradas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantidade": 0
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field")
                        .value("quantidade"))
                .andExpect(jsonPath("$[0].message")
                        .value("Quantidade deve ser maior que zero"));

        verifyNoInteractions(movimentacaoEstoqueService);
    }

    @Test
    void deveRetornarErroQuandoQuantidadeForNegativa() throws Exception {

        mockMvc.perform(post("/produtos/1/entradas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantidade": -5
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field")
                        .value("quantidade"))
                .andExpect(jsonPath("$[0].message")
                        .value("Quantidade deve ser maior que zero"));

        verifyNoInteractions(movimentacaoEstoqueService);
    }

    @Test
    void deveRetornar400QuandoHouverErroDeLeituraDaRequisicao() throws Exception {

        mockMvc.perform(post("/produtos/1/entradas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantidade":
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Dados da requisição inválidos"));

        verifyNoInteractions(movimentacaoEstoqueService);
    }

    @Test
    void deveRetornar400QuandoEstoqueForInsuficiente() throws Exception {

        when(movimentacaoEstoqueService.saida(
                eq(1L),
                any(MovimentacaoEstoqueRequest.class)
        )).thenThrow(new InsufficientStockException());

        mockMvc.perform(post("/produtos/1/saidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantidade": 10
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(movimentacaoEstoqueService)
                .saida(
                        eq(1L),
                        any(MovimentacaoEstoqueRequest.class)
                );
    }
}