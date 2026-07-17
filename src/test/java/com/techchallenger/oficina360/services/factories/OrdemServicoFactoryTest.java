package com.techchallenger.oficina360.services.factories;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.usecases.factories.OrdemServicoFactory;
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
        CriarOrdemServicoDTO dto = criarOrdemServicoDTO();
        Cliente cliente = criarCliente();
        Veiculo veiculo = criarVeiculo();

        OrdemServico resultado = ordemServicoFactory.criar(dto, cliente.getDocumento(), veiculo.getPlaca());

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

    private CriarOrdemServicoDTO criarOrdemServicoDTO() {
        return new CriarOrdemServicoDTO(
                null,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear",
                null
        );
    }

    private Cliente criarCliente() {
        return new Cliente(
                UUID.randomUUID(),
                "12345678901",
                "João da Silva",
                "joao.silva@email.com",
                "11999999999");
    }

    private Veiculo criarVeiculo() {
        return new Veiculo(
                UUID.randomUUID(),
                "ABC1D23",
                "Volkswagen",
                "Gol",
                "2020",
                "12345678901");
    }
}