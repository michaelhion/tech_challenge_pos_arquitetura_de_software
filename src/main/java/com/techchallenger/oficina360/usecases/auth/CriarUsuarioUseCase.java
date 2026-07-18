package com.techchallenger.oficina360.usecases.auth;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.dtos.autenticacao.CriarUsuarioRequestDTO;
import com.techchallenger.oficina360.frameworks.web.exceptions.ConflitoException;
import com.techchallenger.oficina360.frameworks.web.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.frameworks.web.exceptions.RegraDeNegocioException;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.gateways.PasswordEncoderGateway;
import com.techchallenger.oficina360.gateways.UsuarioGateway;
import com.techchallenger.oficina360.mappers.UsuarioMapper;

import java.util.List;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.AUTH_SERV_E_MAIL_JA_CADASTRADO;
import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.constants.Roles.*;

public class CriarUsuarioUseCase {

	private final UsuarioGateway usuarioGateway;
	private final ClienteGateway clienteGateway;
	private final PasswordEncoderGateway passwordEncoderGateway;

	public CriarUsuarioUseCase(
			UsuarioGateway usuarioGateway,
			ClienteGateway clienteGateway,
			PasswordEncoderGateway passwordEncoderGateway
	) {
		this.usuarioGateway = usuarioGateway;
		this.clienteGateway = clienteGateway;
		this.passwordEncoderGateway = passwordEncoderGateway;
	}


	public void executar(CriarUsuarioRequestDTO dto) {

		if (usuarioGateway.existsByEmail(dto.email())) {
			throw new ConflitoException(
					AUTH_SERV_E_MAIL_JA_CADASTRADO
			);
		}

		String role = dto.role().toUpperCase();

		validarRole(role);

		validarCliente(dto, role);

		Usuario usuario = UsuarioMapper.toDomain(dto);

		usuario.setRole(role);

		usuario.setSenha(
				passwordEncoderGateway.criptografar(dto.senha())
		);
		System.out.println("========================================================");
		System.out.println("teste criptografia "+ passwordEncoderGateway.criptografar("abobrinha"));
		System.out.println("========================================================");
		usuarioGateway.salvar(usuario);
	}


	private void validarCliente(
			CriarUsuarioRequestDTO dto,
			String role
	) {

		if (CLIENTE.equals(role)
				&& !clienteGateway.existsByDocumento(dto.documento())) {

			throw new RecursoNaoEncontradoException(
					CLIENTE_NAO_ENCONTRADO
			);
		}
	}


	private void validarRole(String role) {

		List<String> rolesPermitidas = List.of(
				CLIENTE,
				ADMIN,
				MECANICO,
				ATENDENTE
		);

		if (!rolesPermitidas.contains(role)) {
			throw new RegraDeNegocioException(
					"Role " + role + " não é válida"
			);
		}
	}
}
