package com.techchallenger.oficina360.it.ordemservicos;

import com.techchallenger.oficina360.frameworks.dtos.ordemservico.CriarOrdemServicoRequestDTO;
import com.techchallenger.oficina360.it.BaseIT;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import static com.techchallenger.oficina360.it.fixtures.ordemservico.OrderServiceFixture.ordemServicoPlacaDeOutroCliente;
import static com.techchallenger.oficina360.it.fixtures.ordemservico.OrderServiceFixture.ordemServicoValida;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// validar porque precisa limpar o contexto pra rodar sem receber 403
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
public class OrdemServicoCriacaoIT extends BaseIT {


	@Test
	void deveCriarOrdemServico() throws Exception {
		ResultActions resultActions = criarOrdemServico(ordemServicoValida());
		resultActions.andExpect(status().isCreated());
	}

	@Test
	void naoDeveCriarOrdemServicoParaVeiculoDeOutroCliente() throws Exception {

		ResultActions resultActions = criarOrdemServico(ordemServicoPlacaDeOutroCliente());
		resultActions
				.andExpect(status().isUnprocessableContent())
				.andDo(print());
	}

	private ResultActions criarOrdemServico(CriarOrdemServicoRequestDTO os) throws Exception {
		return mockMvc.perform(autenticado(
				post("/ordem-servico/salvar").contentType(MediaType.APPLICATION_JSON)
						.content(json(os)), tokenAdmin()));
	}
}
