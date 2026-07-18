package com.techchallenger.oficina360.usecases.autorizacao;


import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.gateways.UsuarioAutenticadoGateway;
import com.techchallenger.oficina360.gateways.VeiculoGateway;

import java.util.UUID;

import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;


public class AutorizacaoClienteUseCase {


	private final VeiculoGateway veiculoGateway;
	private final OrdemServicoGateway ordemServicoGateway;
	private final UsuarioAutenticadoGateway usuarioAutenticadoGateway;


	public AutorizacaoClienteUseCase(
			VeiculoGateway veiculoGateway,
			OrdemServicoGateway ordemServicoGateway,
			UsuarioAutenticadoGateway usuarioAutenticadoGateway
	) {
		this.veiculoGateway = veiculoGateway;
		this.ordemServicoGateway = ordemServicoGateway;
		this.usuarioAutenticadoGateway = usuarioAutenticadoGateway;
	}



	public boolean podeAcessarClientePorDocumento(String documento) {

		Usuario usuario =
				usuarioAutenticadoGateway.obterUsuarioAtual();


		if (!usuarioValido(usuario)) {
			return false;
		}


		return usuario.getDocumento()
				.equals(normalizarDocumento(documento));
	}



	public boolean podeAcessarVeiculo(String placa) {

		Usuario usuario =
				usuarioAutenticadoGateway.obterUsuarioAtual();


		if (!usuarioValido(usuario)) {
			return false;
		}


		return veiculoGateway.findByPlaca(
						normalizarPlaca(placa)
				)
				.map(veiculo ->
						usuario.getDocumento()
								.equals(veiculo.getClienteDocumento())
				)
				.orElse(false);
	}



	public boolean podeAcessarOrdemServico(UUID id) {

		Usuario usuario =
				usuarioAutenticadoGateway.obterUsuarioAtual();


		if (!usuarioValido(usuario)) {
			return false;
		}


		return ordemServicoGateway.findById(id)
				.map(os ->
						usuario.getDocumento()
								.equals(os.getDocumentoCliente())
				)
				.orElse(false);
	}



	private boolean usuarioValido(Usuario usuario) {

		return usuario != null
				&& usuario.getDocumento() != null
				&& !usuario.getDocumento().isBlank();
	}
}