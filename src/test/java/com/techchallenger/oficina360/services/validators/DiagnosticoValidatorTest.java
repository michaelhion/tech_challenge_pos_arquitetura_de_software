package com.techchallenger.oficina360.services.validators;

import com.techchallenger.oficina360.entities.Estoque;
import com.techchallenger.oficina360.entities.Servico;
import com.techchallenger.oficina360.exceptions.RegraDeNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DiagnosticoValidatorTest {

    private DiagnosticoValidator diagnosticoValidator;

    @BeforeEach
    void setUp() {
        diagnosticoValidator = new DiagnosticoValidator();
    }

    @Test
    void deveValidarDiagnosticoComSucesso() {
        List<String> codigosServicos = List.of("SRV-TROCA-OLEO");

        Map<String, Servico> servicosPorCodigo = Map.of(
                "SRV-TROCA-OLEO",
                criarServico("SRV-TROCA-OLEO")
        );

        Map<String, Integer> quantidadeEstoquePorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                2
        );

        Map<String, Estoque> estoquesPorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                criarEstoque("EST-FILTRO-OLEO", 10, 2)
        );

        assertDoesNotThrow(() -> diagnosticoValidator.validar(
                codigosServicos,
                quantidadeEstoquePorCodigo,
                servicosPorCodigo,
                estoquesPorCodigo
        ));
    }

    @Test
    void deveLancarErroQuandoNaoInformarServicos() {
        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> diagnosticoValidator.validar(
                        List.of(),
                        Map.of(),
                        Map.of(),
                        Map.of()
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());
        assertTrue(exception.getMensagens()
                .contains("Informe ao menos um serviço para o diagnóstico"));
    }

    @Test
    void deveLancarErroQuandoServicoNaoForEncontrado() {

        List<String> codigosServicos = List.of("SRV-TROCA-OLEO","SRV-ALINHAMENTO");

        Map<String, Integer> quantidadeEstoquePorCodigo = Map.of();

        Map<String, Servico> servicosPorCodigo = Map.of();

        Map<String, Estoque> estoquesPorCodigo = Map.of();

        RegraDeNegocioException exception =
                assertThrows(
                        RegraDeNegocioException.class,
                        () -> diagnosticoValidator.validar(
                                codigosServicos,
                                quantidadeEstoquePorCodigo,
                                servicosPorCodigo,
                                estoquesPorCodigo
                        )
                );

        assertEquals("Diagnóstico inválido", exception.getMessage());
        assertTrue(exception.getMensagens()
                .contains("Serviços não encontrados: SRV-TROCA-OLEO, SRV-ALINHAMENTO"));
    }

    @Test
    void deveLancarErroQuandoItemEstoqueNaoForEncontrado() {
        List<String> codigosServicos = List.of("SRV-TROCA-OLEO");

        Map<String, Servico> servicosPorCodigo = Map.of(
                "SRV-TROCA-OLEO",
                criarServico("SRV-TROCA-OLEO")
        );

        Map<String, Integer> quantidadeEstoquePorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                2
        );

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> diagnosticoValidator.validar(
                        codigosServicos,
                        quantidadeEstoquePorCodigo,
                        servicosPorCodigo,
                        Map.of()
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());
        assertTrue(exception.getMensagens()
                .contains("Itens de estoque não encontrados: EST-FILTRO-OLEO"));
    }

    @Test
    void deveLancarErroQuandoQuantidadeEstoqueForZero() {
        List<String> codigosServicos = List.of("SRV-TROCA-OLEO");

        Map<String, Servico> servicosPorCodigo = Map.of(
                "SRV-TROCA-OLEO",
                criarServico("SRV-TROCA-OLEO")
        );

        Map<String, Integer> quantidadeEstoquePorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                0
        );

        Map<String, Estoque> estoquesPorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                criarEstoque("EST-FILTRO-OLEO", 10, 2)
        );

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> diagnosticoValidator.validar(
                        codigosServicos,
                        quantidadeEstoquePorCodigo,
                        servicosPorCodigo,
                        estoquesPorCodigo
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());
        assertTrue(exception.getMensagens()
                .contains("Quantidade inválida para o item de estoque EST-FILTRO-OLEO"));
    }

    @Test
    void deveLancarErroQuandoQuantidadeEstoqueForNegativa() {
        List<String> codigosServicos = List.of("SRV-TROCA-OLEO");

        Map<String, Servico> servicosPorCodigo = Map.of(
                "SRV-TROCA-OLEO",
                criarServico("SRV-TROCA-OLEO")
        );

        Map<String, Integer> quantidadeEstoquePorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                -1
        );

        Map<String, Estoque> estoquesPorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                criarEstoque("EST-FILTRO-OLEO", 10, 2)
        );

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> diagnosticoValidator.validar(
                        codigosServicos,
                        quantidadeEstoquePorCodigo,
                        servicosPorCodigo,
                        estoquesPorCodigo
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());
        assertTrue(exception.getMensagens()
                .contains("Quantidade inválida para o item de estoque EST-FILTRO-OLEO"));
    }

    @Test
    void deveLancarErroQuandoQuantidadeEstoqueForNula() {
        List<String> codigosServicos = List.of("SRV-TROCA-OLEO");

        Map<String, Servico> servicosPorCodigo = Map.of(
                "SRV-TROCA-OLEO",
                criarServico("SRV-TROCA-OLEO")
        );

        Map<String, Integer> quantidadeEstoquePorCodigo = new HashMap<>();
        quantidadeEstoquePorCodigo.put("EST-FILTRO-OLEO", null);

        Map<String, Estoque> estoquesPorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                criarEstoque("EST-FILTRO-OLEO", 10, 2)
        );

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> diagnosticoValidator.validar(
                        codigosServicos,
                        quantidadeEstoquePorCodigo,
                        servicosPorCodigo,
                        estoquesPorCodigo
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());
        assertTrue(exception.getMensagens()
                .contains("Quantidade inválida para o item de estoque EST-FILTRO-OLEO"));
    }

    @Test
    void deveLancarErroQuandoEstoqueForInsuficiente() {
        List<String> codigosServicos = List.of("SRV-TROCA-OLEO");

        Map<String, Servico> servicosPorCodigo = Map.of(
                "SRV-TROCA-OLEO",
                criarServico("SRV-TROCA-OLEO")
        );

        Map<String, Integer> quantidadeEstoquePorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                5
        );

        Map<String, Estoque> estoquesPorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                criarEstoque("EST-FILTRO-OLEO", 10, 8)
        );

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> diagnosticoValidator.validar(
                        codigosServicos,
                        quantidadeEstoquePorCodigo,
                        servicosPorCodigo,
                        estoquesPorCodigo
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());
        assertTrue(exception.getMensagens()
                .contains("Estoque insuficiente para EST-FILTRO-OLEO. Solicitado: 5, disponível: 2"));
    }

    @Test
    void deveAcumularMultiplosErros() {
        List<String> codigosServicos = List.of("SRV-INEXISTENTE");

        Map<String, Integer> quantidadeEstoquePorCodigo = Map.of(
                "EST-INEXISTENTE",
                2
        );

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> diagnosticoValidator.validar(
                        codigosServicos,
                        quantidadeEstoquePorCodigo,
                        Map.of(),
                        Map.of()
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());
        assertEquals(2, exception.getMensagens().size());
        assertTrue(exception.getMensagens()
                .contains("Serviços não encontrados: SRV-INEXISTENTE"));
        assertTrue(exception.getMensagens()
                .contains("Itens de estoque não encontrados: EST-INEXISTENTE"));
    }

    @Test
    void deveAcumularErroDeServicoNaoEncontradoEEstoqueInsuficiente() {
        List<String> codigosServicos = List.of("SRV-INEXISTENTE");

        Map<String, Integer> quantidadeEstoquePorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                5
        );

        Map<String, Estoque> estoquesPorCodigo = Map.of(
                "EST-FILTRO-OLEO",
                criarEstoque("EST-FILTRO-OLEO", 10, 8)
        );

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> diagnosticoValidator.validar(
                        codigosServicos,
                        quantidadeEstoquePorCodigo,
                        Map.of(),
                        estoquesPorCodigo
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());
        assertEquals(2, exception.getMensagens().size());
        assertTrue(exception.getMensagens()
                .contains("Serviços não encontrados: SRV-INEXISTENTE"));
        assertTrue(exception.getMensagens()
                .contains("Estoque insuficiente para EST-FILTRO-OLEO. Solicitado: 5, disponível: 2"));
    }

    private Servico criarServico(String codigo) {
        return Servico.builder()
                .id(UUID.randomUUID())
                .codigo(codigo)
                .descricao("Troca de óleo")
                .valor(BigDecimal.valueOf(150.00))
                .build();
    }

    private Estoque criarEstoque(String codigo, Integer quantidade, Integer reservados) {
        return Estoque.builder()
                .id(UUID.randomUUID())
                .codigo(codigo)
                .nome("Filtro de óleo")
                .valor(BigDecimal.valueOf(45.90))
                .quantidade(quantidade)
                .reservados(reservados)
                .build();
    }
}