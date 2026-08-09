package com.techchallenger.oficina360.it.ordemservicos;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.frameworks.adapters.RelogioSistema;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test-jpa")
class OrdemServicoListarIT extends BaseIT {

	private static final String ORDEM_SERVICO_BASE_PATH = "/ordem-servico";
	private static final String SENHA = "$2a$10$ATQgy75GIzx6MsDKQmijxOWrBb5oP7hu/1HBQa1slZfIVQ/fasI7e";
	public static final String DOCUMENTO_CLIENTE = "99999999998";

	@Autowired
	private UsuarioGateway usuarioGateway;

	@Autowired
	private OrdemServicoGateway ordemServicoGateway;

	@Autowired
	private RelogioSistema relogioSistema;

	@BeforeEach
	void setup() {
		Usuario usuarioAdmin = new Usuario(
				null,
				"admin@oficina360.com",
				SENHA,
				"ADMIN",
				"99999999999"
		);

		Usuario usuarioCliente = new Usuario(
				null,
				"cliente@oficina360.com",
				SENHA,
				"CLIENTE",
				DOCUMENTO_CLIENTE
		);

		usuarioGateway.saveAll(List.of(usuarioAdmin,usuarioCliente));

		OrdemServico ordemServico1 = OrdemServico.criar(DOCUMENTO_CLIENTE, "ABC1D23", "qualquer coisa",relogioSistema.agora());
		OrdemServico ordemServico2 = OrdemServico.criar("99999999997", "ABC1D24", "qualquer coisa",relogioSistema.agora());
		ordemServicoGateway.save(ordemServico1);
		ordemServicoGateway.save(ordemServico2);
		SecurityContextHolder.clearContext();
	}

	@Test
	void deveListarOrdemServico() throws Exception {

		mockMvc.perform(autenticado(get(ORDEM_SERVICO_BASE_PATH + "/listar"), tokenAdmin())).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray()).andDo(print())
				.andExpect(jsonPath("$.content.length()").value(2))
				.andExpect(jsonPath("$.pageable.pageNumber").value(0))
				.andExpect(jsonPath("$.totalElements").value(2));

	}

}