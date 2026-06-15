package com.techchallenger.oficina360.services.factories;

import com.techchallenger.oficina360.entities.Estoque;
import com.techchallenger.oficina360.entities.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.entities.OrdemServicoServico;
import com.techchallenger.oficina360.entities.Servico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DiagnosticoFactoryTest {

    private DiagnosticoFactory diagnosticoFactory;

    @BeforeEach
    void setUp() {
        diagnosticoFactory = new DiagnosticoFactory();
    }

    @Test
    void deveCriarServicoDaOrdemServicoAPartirDeServicoCadastrado() {
        Servico servico = Servico.builder()
                .id(UUID.randomUUID())
                .codigo("SRV-TROCA-OLEO")
                .descricao("Troca de óleo")
                .valor(BigDecimal.valueOf(150.00))
                .build();

        OrdemServicoServico resultado = diagnosticoFactory.criarServicoDaOs(servico);

        assertNotNull(resultado);
        assertEquals(servico.getId(), resultado.getServicoId());
        assertEquals("Troca de óleo", resultado.getDescricao());
        assertEquals(BigDecimal.valueOf(150.00), resultado.getValor());
    }

    @Test
    void deveCriarItemEstoqueDaOrdemServicoAPartirDeEstoqueEQuantidade() {
        Estoque estoque = Estoque.builder()
                .id(UUID.randomUUID())
                .codigo("EST-FILTRO-OLEO")
                .nome("Filtro de óleo")
                .valor(BigDecimal.valueOf(45.90))
                .quantidade(20)
                .reservados(5)
                .build();

        OrdemServicoItemEstoque resultado =
                diagnosticoFactory.criarItemEstoqueDaOs(estoque, 2);

        assertNotNull(resultado);
        assertEquals(estoque.getId(), resultado.getEstoqueId());
        assertEquals("Filtro de óleo", resultado.getNome());
        assertEquals(BigDecimal.valueOf(45.90), resultado.getValorUnitario());
        assertEquals(2, resultado.getQuantidade());
        assertEquals(BigDecimal.valueOf(91.80), resultado.getValorTotal());
    }
}