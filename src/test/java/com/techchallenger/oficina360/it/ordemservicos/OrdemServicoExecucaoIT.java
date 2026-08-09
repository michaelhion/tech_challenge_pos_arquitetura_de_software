package com.techchallenger.oficina360.it.ordemservicos;

import com.techchallenger.oficina360.it.BaseIT;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
public class OrdemServicoExecucaoIT extends BaseIT {
	private static final String OS_COM_STATUS_APROVADA = "2b3a19fd-d3c0-4d9f-9738-6fec31269023";
	private static final String OS_COM_STATUS_REPROVADA = "0b49c552-8ba0-4c0b-bcec-42db82526af9";

	@Test
	void deveIniciarExecucao() throws Exception {
		executar(OS_COM_STATUS_APROVADA,tokenAdmin()).andExpect(status().isAccepted()).andDo(print());
	}

	@Test
	void naoDeveIniciarExecucaoDeOrdemDeServicoReprovada() throws Exception {

		executar(OS_COM_STATUS_REPROVADA,tokenAdmin()).andExpect(status().isConflict()).andDo(print());
	}

	@Test
	void clienteNaoDeveIniciarExecucao() throws Exception {
			executar(OS_COM_STATUS_APROVADA,tokenCliente()).andExpect(status().isForbidden()).andDo(print());
	}

	private ResultActions executar(String id,String token) throws Exception {
		return mockMvc.perform(autenticado(
				patch("/ordem-servico/execucao/iniciar/%s".formatted(id)), token));
	}
}
