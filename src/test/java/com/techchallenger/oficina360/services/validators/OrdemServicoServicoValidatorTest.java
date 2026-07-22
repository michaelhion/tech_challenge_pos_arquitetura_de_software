package com.techchallenger.oficina360.services.validators;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.usecases.shared.exception.RegraDeNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrdemServicoServicoValidatorTest {

    private OrdemServicoServicoValidator validator;

    @BeforeEach
    void setUp() {
        validator = new OrdemServicoServicoValidator();
    }

    @Test
    void deveValidarDiagnosticoComSucesso() {

        List<String> codigosServicos = List.of("SRV-TROCA-OLEO");

        Map<String, Servico> servicosPorCodigo = Map.of(
                "SRV-TROCA-OLEO",
                criarServico("SRV-TROCA-OLEO")
        );

        assertDoesNotThrow(() ->
                validator.validar(codigosServicos, servicosPorCodigo)
        );
    }

    @Test
    void deveLancarErroQuandoNaoInformarServicos() {

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> validator.validar(
                        List.of(),
                        Map.of()
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());
        assertTrue(exception.getMensagens()
                .contains("Informe ao menos um serviço para o diagnóstico"));
    }

    @Test
    void deveLancarErroQuandoListaDeServicosForNula() {

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> validator.validar(
                        null,
                        Map.of()
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());
        assertTrue(exception.getMensagens()
                .contains("Informe ao menos um serviço para o diagnóstico"));
    }

    @Test
    void deveLancarErroQuandoServicoNaoForEncontrado() {

        List<String> codigosServicos = List.of(
                "SRV-TROCA-OLEO",
                "SRV-ALINHAMENTO"
        );

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> validator.validar(
                        codigosServicos,
                        Map.of()
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());

        assertTrue(exception.getMensagens().contains(
                "Serviços não encontrados: SRV-TROCA-OLEO, SRV-ALINHAMENTO"
        ));
    }

    @Test
    void deveLancarErroQuandoAlgunsServicosNaoForemEncontrados() {

        List<String> codigosServicos = List.of(
                "SRV-TROCA-OLEO",
                "SRV-ALINHAMENTO"
        );

        Map<String, Servico> servicosPorCodigo = Map.of(
                "SRV-TROCA-OLEO",
                criarServico("SRV-TROCA-OLEO")
        );

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> validator.validar(
                        codigosServicos,
                        servicosPorCodigo
                )
        );

        assertEquals("Diagnóstico inválido", exception.getMessage());

        assertTrue(exception.getMensagens().contains(
                "Serviços não encontrados: SRV-ALINHAMENTO"
        ));
    }

    @Test
    void deveIgnorarCodigosDuplicadosAoValidar() {

        List<String> codigosServicos = List.of(
                "SRV-INEXISTENTE",
                "SRV-INEXISTENTE"
        );

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> validator.validar(
                        codigosServicos,
                        Map.of()
                )
        );

        assertEquals(1, exception.getMensagens().size());

        assertTrue(exception.getMensagens().contains(
                "Serviços não encontrados: SRV-INEXISTENTE"
        ));
    }

    private Servico criarServico(String codigo) {
        return new Servico(
                UUID.randomUUID(),
                "Troca de óleo",
                BigDecimal.valueOf(150.00),
                codigo,
                90
                );
    }
}