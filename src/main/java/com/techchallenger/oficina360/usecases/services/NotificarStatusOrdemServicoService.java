package com.techchallenger.oficina360.usecases.services;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dominio.MensagemEmail;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.gateways.NotificacaoEmailGateway;

public class NotificarStatusOrdemServicoService {

	private final ClienteGateway clienteGateway;
	private final NotificacaoEmailGateway emailGateway;

	public NotificarStatusOrdemServicoService(ClienteGateway clienteGateway, NotificacaoEmailGateway emailGateway) {
		this.clienteGateway = clienteGateway;
		this.emailGateway = emailGateway;
	}

	public void notificar(OrdemServico ordemServico) {
		Cliente cliente = buscarCliente(ordemServico);

		if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
			return;
		}

		MensagemEmail mensagem = criarMensagem(cliente, ordemServico);

		emailGateway.enviar(mensagem);
	}

	private Cliente buscarCliente(OrdemServico ordemServico) {
		return clienteGateway.findByDocumento(ordemServico.getDocumentoCliente())
				.orElseThrow(() -> new IllegalStateException("Cliente da Ordem de Serviço " + "não foi encontrado."));
	}

	private MensagemEmail criarMensagem(Cliente cliente, OrdemServico ordemServico) {
		String assunto = "Atualização da Ordem de Serviço " + ordemServico.getId();

		String conteudo = """
				Olá, %s.
				
				A sua Ordem de Serviço foi atualizada.
				
				Número da OS: %s
				Veículo: %s
				Status atual: %s
				Problema informado: %s
				Valor total: R$ %s
				
				Oficina360
				""".formatted(cliente.getNome(), ordemServico.getId(), ordemServico.getPlacaVeiculo(),
				formatarStatus(ordemServico.getOrdemDeServicoStatus().name()), ordemServico.getDescricaoProblema(),
				ordemServico.getValorOs());

		return new MensagemEmail(cliente.getEmail(), assunto, conteudo);
	}

	private String formatarStatus(String status) {
		String texto = status.replace("_", " ").toLowerCase();

		return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
	}
}