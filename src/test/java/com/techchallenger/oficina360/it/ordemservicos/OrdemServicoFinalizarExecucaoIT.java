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

//todo validar porque precisa limpar o contexto pra rodar sem receber 403
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
public class OrdemServicoFinalizarExecucaoIT extends BaseIT {
	//todo validar a possibilidade de trocar massa de dados do flyway para jpa em tempo de execução
	private static final String OS_COM_STATUS_RECEBIDA = "76dba7d9-2ded-426f-aae8-fd8f8506a7cc";
	private static final String OS_COM_STATUS_EM_EXECUCAO = "4baecc4b-57d2-419b-b080-ae6615a44052";

	@Test
	void deveFinalizarExecucao() throws Exception {

		finalizar(OS_COM_STATUS_EM_EXECUCAO,tokenAdmin()).andExpect(status().isAccepted()).andDo(print());
	}

	@Test
	void clienteNaodeveFinalizarExecucao() throws Exception {
		finalizar(OS_COM_STATUS_EM_EXECUCAO,tokenCliente1()).andExpect(status().isForbidden()).andDo(print());
	}

	@Test
	void naoDeveFinalizarOrdemNaoIniciada() throws Exception {
			finalizar(OS_COM_STATUS_RECEBIDA,tokenAdmin()).andExpect(status().isConflict()).andDo(print());
	}



	private ResultActions finalizar(String id,String token) throws Exception {
		return mockMvc.perform(autenticado(
				patch("/ordem-servico/execucao/finalizar/%s".formatted(id)), token));
	}
}
