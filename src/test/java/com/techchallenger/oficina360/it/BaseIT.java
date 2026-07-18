package com.techchallenger.oficina360.it;

import com.techchallenger.oficina360.gateways.TokenGateway;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import static com.techchallenger.oficina360.it.fixtures.ordemservico.OrdemServicoFixture.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public abstract class BaseIT {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected TokenGateway tokenGateway;

	protected String tokenAdmin() {

		return tokenGateway.gerarToken(gerarUsuarioAdmin());
	}

	protected String tokenCliente() {

		return tokenGateway.gerarToken(gerarUsuarioCliente());
	}

	protected String tokenCliente1() {

		return tokenGateway.gerarToken(gerarUsuarioCliente1());
	}

	protected String tokenCliente2() {

		return tokenGateway.gerarToken(gerarUsuarioCliente2());
	}

	protected MockHttpServletRequestBuilder autenticado(MockHttpServletRequestBuilder request, String token) {

		return request.header("Authorization", "Bearer " + token);
	}

	protected String json(Object obj){
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JacksonException e) {
			throw new RuntimeException(e);
		}
	}

}
