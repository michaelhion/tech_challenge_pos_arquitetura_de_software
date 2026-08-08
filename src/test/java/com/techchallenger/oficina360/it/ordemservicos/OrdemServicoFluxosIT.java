package com.techchallenger.oficina360.it.ordemservicos;

import com.jayway.jsonpath.JsonPath;
import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
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

import java.util.UUID;

import static com.techchallenger.oficina360.enums.OrdemDeServicoStatus.EM_EXECUCAO;
import static com.techchallenger.oficina360.enums.OrdemDeServicoStatus.FINALIZADA;
import static com.techchallenger.oficina360.it.fixtures.ordemservico.OrderServiceFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//todo validar porque precisa limpar o contexto pra rodar sem receber 403
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
public class OrdemServicoFluxosIT extends BaseIT {

	@MockitoBean
	private NotificacaoEmailGateway notificacaoEmailGateway;

	@Autowired
	private OrdemServicoGateway ordemServicoGateway;

	@Autowired
	private EstoqueGateway estoqueGateway;

	@Test
	void devePassarPorTodosStatus() throws Exception {

		String idString = criarOrdemServico();
		UUID uuid = UUID.fromString(idString);
		System.out.println("======================================================================");
		System.out.println("criou a ordem de servico");
		System.out.println("======================================================================");
		mockMvc.perform(
						autenticado(patch("/ordem-servico/%s/diagnostico".formatted(idString)), tokenAdmin()).contentType(
								MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(diagnosticoDTOValido())))
				.andExpect(status().isOk()).andExpect(jsonPath("$.ordemDeServicoStatus").value("AGUARDANDO_APROVACAO"))
				.andDo(print());

		System.out.println("======================================================================");
		System.out.println("fez o diagnostico");
		System.out.println("======================================================================");

		mockMvc.perform(patch(("/ordem-servico/clientes/aprovacao/%s").formatted(idString)).header("Authorization",
								"Bearer " + tokenCliente2()).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(aprovacaoOrdemServicoDTOValido()))).andExpect(status().isAccepted()).andDo(print());

		System.out.println("======================================================================");
		System.out.println("aprovou a ordem de servico");
		System.out.println("======================================================================");

		mockMvc.perform(patch("/ordem-servico/execucao/iniciar/%s".formatted(idString)).header("Authorization",
				"Bearer " + tokenAdmin())).andExpect(status().isAccepted()).andDo(print());

		System.out.println("======================================================================");
		System.out.println("iniciou execucao");
		System.out.println("======================================================================");

		OrdemServico os = ordemServicoGateway.findById(uuid).orElseThrow();
		assertEquals(EM_EXECUCAO, os.getOrdemDeServicoStatus());

		mockMvc.perform(patch( "/ordem-servico/execucao/finalizar/%s".formatted(idString)).header("Authorization",
				"Bearer " + tokenAdmin())).andExpect(status().isAccepted()).andDo(print());

		System.out.println("======================================================================");
		System.out.println("finalizou");
		System.out.println("======================================================================");

		os = ordemServicoGateway.findById(uuid).orElseThrow();
		assertEquals(FINALIZADA, os.getOrdemDeServicoStatus());
	}

	private String criarOrdemServico() throws Exception {
		String response = mockMvc.perform(autenticado(
						post("/ordem-servico/salvar").contentType(MediaType.APPLICATION_JSON)
								.content(json(ordemServicoValida())), tokenAdmin())).andExpect(status().isCreated()).andReturn()
				.getResponse().getContentAsString();
		UUID id = UUID.fromString(JsonPath.read(response, "$.id"));;
		return id.toString();
	}

	@Test
	void deveConsumirEstoqueQuandoFinalizarExecucao() throws Exception {

		String ordemServicoId = criarOrdemServico();

		mockMvc.perform(autenticado(patch( "/ordem-servico/{id}/diagnostico", ordemServicoId),
						tokenAdmin()).contentType(MediaType.APPLICATION_JSON).content(json(diagnosticoDTOValido())))
				.andExpect(status().isOk());

		aprovar(ordemServicoId,aprovacaoOrdemServicoDTOValido(),tokenCliente2());
		mockMvc.perform(patch("/ordem-servico/execucao/iniciar/%s".formatted(ordemServicoId)).header(
				"Authorization", "Bearer " + tokenAdmin())).andExpect(status().isAccepted());

		mockMvc.perform(patch("/ordem-servico/execucao/finalizar/%s".formatted(ordemServicoId)).header(
				"Authorization", "Bearer " + tokenAdmin())).andExpect(status().isAccepted());

		Estoque estoqueDepois = estoqueGateway.findByCodigo("PNEU-205-55-R16").orElseThrow();

		assertEquals(16, estoqueDepois.getQuantidade());
		assertEquals(1, estoqueDepois.getReservados());
		assertEquals(15, estoqueDepois.getDisponiveis());;
	}

	private ResultActions fazerPatch(String endpoint,String id) throws Exception {
		return mockMvc.perform(autenticado(
				patch("/ordem-servico/execucao/finalizar/%s".formatted(id)),tokenAdmin()));
	}

	private ResultActions aprovar(String id,AprovacaoOrdemServicoDTO aprovacaoOrdemServicoDTO, String token) throws Exception {
		return mockMvc.perform(autenticado(patch(("/ordem-servico/clientes/aprovacao/%s").formatted(
				id)), token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(aprovacaoOrdemServicoDTO)));
	}

}
