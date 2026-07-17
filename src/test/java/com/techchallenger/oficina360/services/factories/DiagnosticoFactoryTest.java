package com.techchallenger.oficina360.services.factories;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.usecases.factories.DiagnosticoFactory;
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
        Servico servico = new Servico(
                UUID.randomUUID(),
                "Troca de óleo",
                BigDecimal.valueOf(150.00),
                "SRV-TROCA-OLEO",
                90);

        OrdemServicoServico resultado = diagnosticoFactory.criarServicoDaOs(servico);

        assertNotNull(resultado);
        assertEquals(servico.getId(), resultado.getServicoId());
        assertEquals("Troca de óleo", resultado.getDescricao());
        assertEquals(BigDecimal.valueOf(150.00), resultado.getValor());
    }

    @Test
    void deveCriarItemEstoqueDaOrdemServicoAPartirDeEstoqueEQuantidade() {
        Estoque estoqueEntity = new Estoque(
                UUID.randomUUID(),
                "Filtro de óleo",
                BigDecimal.valueOf(45.90),
                20,
                5,
                "EST-FILTRO-OLEO"
                );

        OrdemServicoItemEstoque resultado =
                diagnosticoFactory.criarItemEstoqueDaOs(estoqueEntity, 2);

        assertNotNull(resultado);
        assertEquals(estoqueEntity.getId(), resultado.getEstoqueId());
        assertEquals("Filtro de óleo", resultado.getNome());
        assertEquals(BigDecimal.valueOf(45.90), resultado.getValorUnitario());
        assertEquals(2, resultado.getQuantidade());
        assertEquals(BigDecimal.valueOf(91.80), resultado.getValorTotal());
    }
}