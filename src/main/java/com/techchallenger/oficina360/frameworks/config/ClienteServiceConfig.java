package com.techchallenger.oficina360.frameworks.config;

import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.usecases.cliente.AtualizarClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.BuscarClientePorDocumentoUseCase;
import com.techchallenger.oficina360.usecases.cliente.CadastrarClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.ExcluirClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.ListarClientesUseCase;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteServiceConfig {

	@Bean
	@Transactional
	public CadastrarClienteUseCase cadastrarClienteUseCase(ClienteGateway clienteGateway) {
		return new CadastrarClienteUseCase(clienteGateway);

	}

	@Bean
	@Transactional
	public AtualizarClienteUseCase atualizarClienteUseCase(ClienteGateway clienteGateway) {
		return new AtualizarClienteUseCase(clienteGateway);

	}
	@Bean
	@Transactional
	public BuscarClientePorDocumentoUseCase buscarClientePorDocumentoUseCase(ClienteGateway clienteGateway) {
		return new BuscarClientePorDocumentoUseCase(clienteGateway);

	}
	@Bean
	@Transactional
	public ListarClientesUseCase listarClientesUseCase(ClienteGateway clienteGateway) {
		return new ListarClientesUseCase(clienteGateway);

	}
	@Bean
	@Transactional
	public ExcluirClienteUseCase excluirClienteUseCase(ClienteGateway clienteGateway) {
		return new ExcluirClienteUseCase(clienteGateway);

	}
}
