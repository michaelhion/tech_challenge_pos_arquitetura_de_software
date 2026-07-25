package com.techchallenger.oficina360.it.ordemservicos;

import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.gateways.UsuarioGateway;
import com.techchallenger.oficina360.it.BaseIT;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//todo validar porque precisa limpar o contexto pra rodar sem receber 403
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
class OrdemServicoListarIT extends BaseIT {

	private static final String ORDEM_SERVICO_BASE_PATH = "/ordem-servico";

	@Autowired
	private UsuarioGateway usuarioGateway;

	@Autowired
	private OrdemServicoGateway ordemServicoGateway;

	@BeforeEach
	void setup() {
		//todo validar a possibilidade de trocar massa de dados do flyway para jpa em tempo de execução
//		usuarioGateway.salvar(new Usuario(
//				null,
//				"admin@oficina360.com",
//				"$2a$10$ATQgy75GIzx6MsDKQmijxOWrBb5oP7hu/1HBQa1slZfIVQ/fasI7e",
//				"ADMIN",
//				"99999999999"
//		));
//
//		ordemServicoGateway.save(new OrdemServico(
//				null,
//				"99999999999",
//				"ABC1D23",
//				LocalDateTime.now(),
//				null,
//				"Veículo apresenta ruído ao frear e vibração no volante.",
//				null,
//				null,
//				List.of(new OrdemServicoServico()),
//				List.of(new OrdemServicoItemEstoque(UUID.randomUUID(),UUID.randomUUID(),"teste",BigDecimal.TEN,5)),
//				null,
//				null
//		));
		SecurityContextHolder.clearContext();
	}

	@Test
	void deveListarOrdemServico() throws Exception {

		mockMvc.perform(autenticado(get(ORDEM_SERVICO_BASE_PATH + "/listar"), tokenAdmin())).andExpect(status().isOk())
				.andExpect(jsonPath("$.ordemServico").isArray())
				.andDo(print());

	}


}