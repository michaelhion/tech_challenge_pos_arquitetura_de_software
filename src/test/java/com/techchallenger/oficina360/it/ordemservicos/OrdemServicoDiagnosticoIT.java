package com.techchallenger.oficina360.it.ordemservicos;

import com.jayway.jsonpath.JsonPath;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoRequestDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.gateways.NotificacaoEmailGateway;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.it.BaseIT;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.UUID;

import static com.techchallenger.oficina360.it.fixtures.ordemservico.OrderServiceFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
public class OrdemServicoDiagnosticoIT extends BaseIT {

	private static final String OS_COM_STATUS_RECEBIDA = "76dba7d9-2ded-426f-aae8-fd8f8506a7cc";

	@Autowired
	private EstoqueGateway estoqueGateway;

	@Autowired
	private OrdemServicoGateway ordemServicoGateway;

	@MockitoBean
	private NotificacaoEmailGateway notificacaoEmailGateway;

	private UUID extrairIdDoResponse(ResultActions resultActions) throws Exception {
		UUID id = UUID.fromString(JsonPath.read(resultActions.andReturn().getResponse().getContentAsString(), "$.id"));
		return id;
	}

	@Test
	void deveDiagnosticar() throws Exception {
				diagnosticar(diagnosticoDTOValido(),OS_COM_STATUS_RECEBIDA,tokenAdmin())
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	void naoDeveDiagnosticarQuandoItemEstoqueNaoExiste() throws Exception {

		UUID ordemServicoId = extrairIdDoResponse(criarOrdemServico(ordemServicoValida()));

		diagnosticar(diagnosticoComItemEstoqueInexistente(), ordemServicoId.toString(), tokenAdmin())
				.andExpect(status().isUnprocessableContent())
				.andDo(print());
	}

	@Test
	void naoDeveAdicionarParcialmenteServicos() throws Exception {

		UUID ordemServicoId = extrairIdDoResponse(criarOrdemServico(ordemServicoValida()));

		diagnosticar(diagnosticoComServicoValidoEInvalido(), ordemServicoId.toString(), tokenAdmin()).andExpect(
				status().isUnprocessableContent());
		OrdemServico ordemServico = ordemServicoGateway.findById(ordemServicoId).orElseThrow();

		assertTrue(ordemServico.getServicos().isEmpty());
	}

	@Test
	void naoDeveDiagnosticarQuandoServicoNaoExiste() throws Exception {

		UUID ordemServicoId = extrairIdDoResponse(criarOrdemServico(ordemServicoValida()));

		diagnosticar(diagnosticoComServicoInexistente(), ordemServicoId.toString(), tokenAdmin())
				.andExpect(status().isUnprocessableContent()).andDo(print());
	}

	@Test
	void deveAdicionarServicosAoDiagnostico() throws Exception {

		UUID ordemServicoId = extrairIdDoResponse(criarOrdemServico(ordemServicoValida()));
		diagnosticar(diagnosticoDTOValido(), ordemServicoId.toString(), tokenAdmin())
				.andExpect(status().isOk());

		OrdemServico ordemServico = ordemServicoGateway.findById(ordemServicoId).orElseThrow();

		assertAll(() -> assertFalse(ordemServico.getServicos().isEmpty()),
				() -> assertEquals(1, ordemServico.getServicos().size()),
				() -> assertTrue(ordemServico.getValorServicos().compareTo(BigDecimal.ZERO) > 0));
	}

	@Test
	void naoDeveReservarEstoqueParcialmente() throws Exception {

		UUID ordemServicoId = extrairIdDoResponse(criarOrdemServico(ordemServicoValida()));
		diagnosticar(diagnosticoComEstoqueValidoEInvalido(), ordemServicoId.toString(), tokenAdmin())
				.andExpect(status().isUnprocessableContent());

		OrdemServico ordemServico = ordemServicoGateway.findById(ordemServicoId).orElseThrow();

		assertTrue(ordemServico.getItensEstoque().isEmpty());
	}

	@Test
	void deveDiagnosticarOrdemComServicoEstoqueEValoresFinanceiros() throws Exception {

		UUID ordemServicoId = extrairIdDoResponse(criarOrdemServico(ordemServicoValida()));

		String response = diagnosticar(diagnosticoDTOValido(),ordemServicoId.toString(),tokenAdmin())
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ordemDeServicoStatus").value("AGUARDANDO_APROVACAO"))
				.andExpect(jsonPath("$.dadosFinanceiros").exists())
				.andExpect(jsonPath("$.dadosFinanceiros.servicos").isArray())
				.andExpect(jsonPath("$.dadosFinanceiros.servicos").isNotEmpty())
				.andExpect(jsonPath("$.dadosFinanceiros.pecasInsumos").isArray())
				.andExpect(jsonPath("$.dadosFinanceiros.pecasInsumos").isNotEmpty()).andReturn().getResponse()
				.getContentAsString();

		Number valorServicos = JsonPath.read(response, "$.dadosFinanceiros.valorServicos");

		Number valorPecasInsumos = JsonPath.read(response, "$.dadosFinanceiros.valorPecasInsumos");

		Number valorTotal = JsonPath.read(response, "$.dadosFinanceiros.valorTotal");

		assertAll(() -> assertTrue(valorServicos.doubleValue() > 0, "O valor dos serviços deveria ser maior que zero"),
				() -> assertTrue(valorPecasInsumos.doubleValue() > 0, "O valor das peças deveria ser maior que zero"),
				() -> assertTrue(valorTotal.doubleValue() > 0, "O valor total da OS deveria ser maior que zero"),
				() -> assertEquals(0, BigDecimal.valueOf(valorTotal.doubleValue()).compareTo(
								BigDecimal.valueOf(valorServicos.doubleValue())
										.add(BigDecimal.valueOf(valorPecasInsumos.doubleValue()))),
						"O valor total deve ser igual à soma de serviços e peças"));
	}

	private ResultActions diagnosticar(DiagnosticoDTO diagnosticoDTO, String id, String token) throws Exception {
		return mockMvc.perform(autenticado(patch("/ordem-servico/{id}/diagnostico", id),
				token).contentType(MediaType.APPLICATION_JSON)
				.content(json(diagnosticoDTO)));
	}

	private ResultActions criarOrdemServico(CriarOrdemServicoRequestDTO os) throws Exception {
		return mockMvc.perform(autenticado(
				post("/ordem-servico/salvar").contentType(MediaType.APPLICATION_JSON)
						.content(json(os)), tokenAdmin()));
	}
}
