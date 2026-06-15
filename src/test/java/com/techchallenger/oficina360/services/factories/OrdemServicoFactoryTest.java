package com.techchallenger.oficina360.services.factories;

import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.entities.Cliente;
import com.techchallenger.oficina360.entities.OrdemServico;
import com.techchallenger.oficina360.entities.Veiculo;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrdemServicoFactoryTest {

    private OrdemServicoFactory ordemServicoFactory;

    @BeforeEach
    void setUp() {
        ordemServicoFactory = new OrdemServicoFactory();
    }

    @Test
    void deveCriarOrdemServicoComDadosIniciaisCorretos() {
        OrdemServicoDTO dto = criarOrdemServicoDTO();
        Cliente cliente = criarCliente();
        Veiculo veiculo = criarVeiculo();

        OrdemServico resultado = ordemServicoFactory.criar(dto, cliente, veiculo);

        assertNotNull(resultado);
        assertEquals("12345678901", resultado.getDocumentoCliente());
        assertEquals("ABC1D23", resultado.getPlacaVeiculo());
        assertEquals("Veículo apresenta ruído ao frear", resultado.getDescricaoProblema());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, resultado.getOrdemDeServicoStatus());
        assertNotNull(resultado.getDtHoraAbertura());

        assertEquals(BigDecimal.ZERO, resultado.getValorServicos());
        assertEquals(BigDecimal.ZERO, resultado.getValorPecasInsumos());
        assertEquals(BigDecimal.ZERO, resultado.getValorOs());
    }

    private OrdemServicoDTO criarOrdemServicoDTO() {
        return new OrdemServicoDTO(
                null,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear",
                null,
                null
        );
    }

    private Cliente criarCliente() {
        return Cliente.builder()
                .id(UUID.randomUUID())
                .documento("12345678901")
                .nome("João da Silva")
                .email("joao.silva@email.com")
                .telefone("11999999999")
                .build();
    }

    private Veiculo criarVeiculo() {
        return Veiculo.builder()
                .id(UUID.randomUUID())
                .placa("ABC1D23")
                .marca("Volkswagen")
                .modelo("Gol")
                .ano("2020")
                .clienteDocumento("12345678901")
                .build();
    }
}