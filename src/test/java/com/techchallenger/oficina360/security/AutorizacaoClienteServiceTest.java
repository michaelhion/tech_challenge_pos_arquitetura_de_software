package com.techchallenger.oficina360.security;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.entities.VeiculoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.frameworks.persistence.repositories.VeiculoRepository;
import com.techchallenger.oficina360.frameworks.security.UsuarioSecurityDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutorizacaoClienteServiceTest {

	@Mock
	private VeiculoRepository veiculoRepository;

	@Mock
	private OrdemServicosRepository ordemServicosRepository;

	@Mock
	private Authentication authentication;

	private AutorizacaoClienteService service;

	@BeforeEach
	void setup() {
		service = new AutorizacaoClienteService(
				veiculoRepository,
				ordemServicosRepository
		);
	}

	private UsuarioSecurityDetails usuarioAutenticado(String documento) {
		Usuario usuario = new Usuario(
				UUID.randomUUID(),
				"cliente@teste.com",
				"senha",
				"CLIENTE",
				documento
		);

		return new UsuarioSecurityDetails(usuario);
	}

	@Test
	void devePermitirAcessoAoProprioDocumento() {

		when(authentication.getPrincipal())
				.thenReturn(usuarioAutenticado("12345678901"));

		boolean resultado =
				service.podeAcessarClientePorDocumento(
						"12345678901",
						authentication
				);

		assertTrue(resultado);
	}

	@Test
	void naoDevePermitirAcessoADocumentoDeOutroCliente() {

		when(authentication.getPrincipal())
				.thenReturn(usuarioAutenticado("12345678901"));

		boolean resultado =
				service.podeAcessarClientePorDocumento(
						"99999999999",
						authentication
				);

		assertFalse(resultado);
	}

	@Test
	void naoDevePermitirQuandoAuthenticationForNull() {

		boolean resultado =
				service.podeAcessarClientePorDocumento(
						"12345678901",
						null
				);

		assertFalse(resultado);
	}

	@Test
	void naoDevePermitirQuandoPrincipalNaoForUsuarioSecurityDetails() {

		when(authentication.getPrincipal())
				.thenReturn("qualquer-coisa");

		boolean resultado =
				service.podeAcessarClientePorDocumento(
						"12345678901",
						authentication
				);

		assertFalse(resultado);
	}

	@Test
	void devePermitirAcessoAoVeiculoDoCliente() {

		VeiculoEntity veiculo = VeiculoEntity.builder()
				.placa("ABC1D23")
				.clienteDocumento("12345678901")
				.build();

		when(authentication.getPrincipal())
				.thenReturn(usuarioAutenticado("12345678901"));

		when(veiculoRepository.findByPlaca("ABC1D23"))
				.thenReturn(Optional.of(veiculo));

		boolean resultado =
				service.podeAcessarVeiculo(
						"abc1d23",
						authentication
				);

		assertTrue(resultado);
	}

	@Test
	void naoDevePermitirAcessoAoVeiculoDeOutroCliente() {

		VeiculoEntity veiculo = VeiculoEntity.builder()
				.placa("ABC1D23")
				.clienteDocumento("99999999999")
				.build();

		when(authentication.getPrincipal())
				.thenReturn(usuarioAutenticado("12345678901"));

		when(veiculoRepository.findByPlaca("ABC1D23"))
				.thenReturn(Optional.of(veiculo));

		boolean resultado =
				service.podeAcessarVeiculo(
						"ABC1D23",
						authentication
				);

		assertFalse(resultado);
	}

	@Test
	void naoDevePermitirQuandoVeiculoNaoExistir() {

		when(authentication.getPrincipal())
				.thenReturn(usuarioAutenticado("12345678901"));

		when(veiculoRepository.findByPlaca(anyString()))
				.thenReturn(Optional.empty());

		boolean resultado =
				service.podeAcessarVeiculo(
						"ABC1D23",
						authentication
				);

		assertFalse(resultado);
	}

	@Test
	void devePermitirAcessoAOrdemServicoDoCliente() {

		UUID id = UUID.randomUUID();

		OrdemServicoEntity ordem = new OrdemServicoEntity();
		ordem.setDocumentoCliente("12345678901");

		when(authentication.getPrincipal())
				.thenReturn(usuarioAutenticado("12345678901"));

		when(ordemServicosRepository.findById(id))
				.thenReturn(Optional.of(ordem));

		boolean resultado =
				service.podeAcessarOrdemServico(
						id,
						authentication
				);

		assertTrue(resultado);
	}

	@Test
	void naoDevePermitirAcessoAOrdemServicoDeOutroCliente() {

		UUID id = UUID.randomUUID();

		OrdemServicoEntity ordem = new OrdemServicoEntity();
		ordem.setDocumentoCliente("99999999999");

		when(authentication.getPrincipal())
				.thenReturn(usuarioAutenticado("12345678901"));

		when(ordemServicosRepository.findById(id))
				.thenReturn(Optional.of(ordem));

		boolean resultado =
				service.podeAcessarOrdemServico(
						id,
						authentication
				);

		assertFalse(resultado);
	}

	@Test
	void naoDevePermitirQuandoOrdemServicoNaoExistir() {

		UUID id = UUID.randomUUID();

		when(authentication.getPrincipal())
				.thenReturn(usuarioAutenticado("12345678901"));

		when(ordemServicosRepository.findById(id))
				.thenReturn(Optional.empty());

		boolean resultado =
				service.podeAcessarOrdemServico(
						id,
						authentication
				);

		assertFalse(resultado);
	}
}