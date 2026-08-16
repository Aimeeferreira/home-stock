package com.home.stock.controller;

import com.home.stock.dto.response.MovimentacaoEstoqueResponse;
import com.home.stock.entity.TipoMovimentacao;
import com.home.stock.service.MovimentacaoEstoqueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovimentacaoEstoqueController.class)
class MovimentacaoEstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Test
    void deveListarTodasAsMovimentacoes() throws Exception {

        MovimentacaoEstoqueResponse response =
                new MovimentacaoEstoqueResponse(
                        1L,
                        1L,
                        TipoMovimentacao.ENTRADA,
                        BigDecimal.valueOf(5),
                        LocalDateTime.of(2026, 8, 16, 12, 0)
                );

        when(movimentacaoEstoqueService.listar(
                null,
                null,
                null,
                null,
                null
        )).thenReturn(List.of(response));

        mockMvc.perform(get("/movimentacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].produtoId").value(1))
                .andExpect(jsonPath("$[0].tipo").value("ENTRADA"))
                .andExpect(jsonPath("$[0].quantidade").value(5));

        verify(movimentacaoEstoqueService).listar(
                null,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void deveFiltrarPorTipo() throws Exception {

        when(movimentacaoEstoqueService.listar(
                TipoMovimentacao.ENTRADA,
                null,
                null,
                null,
                null
        )).thenReturn(List.of());

        mockMvc.perform(get("/movimentacoes")
                        .param("tipo", "ENTRADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(movimentacaoEstoqueService).listar(
                TipoMovimentacao.ENTRADA,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void deveFiltrarPorProduto() throws Exception {

        when(movimentacaoEstoqueService.listar(
                null,
                "arroz",
                null,
                null,
                null
        )).thenReturn(List.of());

        mockMvc.perform(get("/movimentacoes")
                        .param("produto", "arroz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(movimentacaoEstoqueService).listar(
                null,
                "arroz",
                null,
                null,
                null
        );
    }

    @Test
    void deveFiltrarPorCategoria() throws Exception {

        when(movimentacaoEstoqueService.listar(
                null,
                null,
                "Alimentação",
                null,
                null
        )).thenReturn(List.of());

        mockMvc.perform(get("/movimentacoes")
                        .param("categoria", "Alimentação"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(movimentacaoEstoqueService).listar(
                null,
                null,
                "Alimentação",
                null,
                null
        );
    }

    @Test
    void deveFiltrarPorPeriodo() throws Exception {

        LocalDateTime inicio =
                LocalDateTime.of(2026, 8, 1, 0, 0);

        LocalDateTime fim =
                LocalDateTime.of(2026, 8, 31, 23, 59);

        when(movimentacaoEstoqueService.listar(
                null,
                null,
                null,
                inicio,
                fim
        )).thenReturn(List.of());

        mockMvc.perform(get("/movimentacoes")
                        .param("inicio", "2026-08-01T00:00:00")
                        .param("fim", "2026-08-31T23:59:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(movimentacaoEstoqueService).listar(
                null,
                null,
                null,
                inicio,
                fim
        );
    }

    @Test
    void deveCombinarFiltros() throws Exception {

        LocalDateTime inicio =
                LocalDateTime.of(2026, 8, 1, 0, 0);

        LocalDateTime fim =
                LocalDateTime.of(2026, 8, 31, 23, 59);

        when(movimentacaoEstoqueService.listar(
                TipoMovimentacao.SAIDA,
                "arroz",
                "Alimentação",
                inicio,
                fim
        )).thenReturn(List.of());

        mockMvc.perform(get("/movimentacoes")
                        .param("tipo", "SAIDA")
                        .param("produto", "arroz")
                        .param("categoria", "Alimentação")
                        .param("inicio", "2026-08-01T00:00:00")
                        .param("fim", "2026-08-31T23:59:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(movimentacaoEstoqueService).listar(
                TipoMovimentacao.SAIDA,
                "arroz",
                "Alimentação",
                inicio,
                fim
        );
    }
}