package com.techchallenger.oficina360.it.ordemservicos;

import com.jayway.jsonpath.JsonPath;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoRequestDTO;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.it.BaseIT;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.techchallenger.oficina360.it.fixtures.ordemservico.OrderServiceFixture.ordemServicoEditada;
import static com.techchallenger.oficina360.it.fixtures.ordemservico.OrderServiceFixture.ordemServicoValida;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//todo validar porque precisa limpar o contexto pra rodar sem receber 403
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
class OrdemServicoEditarIT extends BaseIT {

	private static final String OS_COM_STATUS_FINALIZADA = "8c7d79a6-370d-4007-a772-a8e22420fbfb";
	private static final String ORDEM_SERVICO_BASE_PATH = "/ordem-servico";

	@Autowired
	private OrdemServicoGateway ordemServicoGateway;

	@BeforeEach
	void setup() {
		SecurityContextHolder.clearContext();
	}


	private UUID criarOrdemServico(CriarOrdemServicoRequestDTO os) throws Exception {
		String response = mockMvc.perform(autenticado(
						post("/ordem-servico/salvar").contentType(MediaType.APPLICATION_JSON)
								.content(json(os)), tokenAdmin())).andExpect(status().isCreated()).andReturn()
				.getResponse().getContentAsString();

		return UUID.fromString(JsonPath.read(response, "$.id"));
	}



	@Test
	void validarStatusFinal() throws Exception {

		mockMvc.perform(
						get(ORDEM_SERVICO_BASE_PATH + "/listar/%s".formatted(OS_COM_STATUS_FINALIZADA)).header("Authorization",
								"Bearer " + tokenAdmin())).andExpect(status().isOk())
				.andExpect(jsonPath("$.ordemDeServicoStatus").value("FINALIZADA"));
	}

	@Test
	void deveEditarOrdemSemCriarNovoRegistro() throws Exception {

		UUID ordemServicoId = criarOrdemServico(ordemServicoValida());

		mockMvc.perform(autenticado(put(ORDEM_SERVICO_BASE_PATH + "/editar" + "/{id}", ordemServicoId),
				tokenAdmin()).contentType(
				MediaType.APPLICATION_JSON).content(json(ordemServicoEditada()))).andExpect(status().isOk());

		OrdemServico ordemServico = ordemServicoGateway.findById(ordemServicoId).orElseThrow();

		assertAll(() -> assertEquals(ordemServicoId, ordemServico.getId()),
				() -> assertEquals("Problema atualizado", ordemServico.getDescricaoProblema()));
	}

}