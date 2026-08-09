package com.techchallenger.oficina360.usecases.auth;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.gateways.PasswordEncoderGateway;
import com.techchallenger.oficina360.gateways.UsuarioGateway;
import com.techchallenger.oficina360.usecases.ordemservico.command.CriarUsuarioCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.usecases.shared.exception.RegraDeNegocioException;
import com.techchallenger.oficina360.usecases.shared.exception.UsuarioJaCadastradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.AUTH_SERV_E_MAIL_JA_CADASTRADO;
import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.constants.Roles.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarUsuarioUseCaseTest {

	private static final String EMAIL = "cliente@oficina360.com";

	private static final String SENHA = "senha-segura";

	private static final String SENHA_CRIPTOGRAFADA = "$2a$10$senhaCriptografada";

	private static final String DOCUMENTO = "12345678901";

	@Mock
	private UsuarioGateway usuarioGateway;

	@Mock
	private ClienteGateway clienteGateway;

	@Mock
	private PasswordEncoderGateway passwordEncoderGateway;

	@InjectMocks
	private CriarUsuarioUseCase useCase;

	private CriarUsuarioCommand commandCliente;

	@BeforeEach
	void setUp() {
		commandCliente = new CriarUsuarioCommand(EMAIL, SENHA, DOCUMENTO, CLIENTE);
	}

	@Test
	void deveCriarUsuarioClienteComSucesso() {
		when(usuarioGateway.existsByEmail(EMAIL)).thenReturn(false);

		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(true);

		when(passwordEncoderGateway.criptografar(SENHA)).thenReturn(SENHA_CRIPTOGRAFADA);

		assertDoesNotThrow(() -> useCase.executar(commandCliente));

		ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);

		verify(usuarioGateway).save(usuarioCaptor.capture());

		Usuario usuarioSalvo = usuarioCaptor.getValue();

		assertAll(() -> assertEquals(EMAIL, usuarioSalvo.getEmail()),
				() -> assertEquals(SENHA_CRIPTOGRAFADA, usuarioSalvo.getSenha()),
				() -> assertEquals(CLIENTE, usuarioSalvo.getRole()),
				() -> assertEquals(DOCUMENTO, usuarioSalvo.getDocumento()));

		verify(usuarioGateway).existsByEmail(EMAIL);

		verify(clienteGateway).existsByDocumento(DOCUMENTO);

		verify(passwordEncoderGateway).criptografar(SENHA);

		verifyNoMoreInteractions(usuarioGateway, clienteGateway, passwordEncoderGateway);
	}

	@Test
	void deveConverterRoleParaMaiusculasAntesDeSalvar() {
		CriarUsuarioCommand command = new CriarUsuarioCommand(EMAIL, SENHA, DOCUMENTO, "cliente");

		when(usuarioGateway.existsByEmail(EMAIL)).thenReturn(false);

		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(true);

		when(passwordEncoderGateway.criptografar(SENHA)).thenReturn(SENHA_CRIPTOGRAFADA);

		useCase.executar(command);

		ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);

		verify(usuarioGateway).save(usuarioCaptor.capture());

		assertEquals(CLIENTE, usuarioCaptor.getValue().getRole());
	}

	@Test
	void deveCriptografarSenhaAntesDeSalvarUsuario() {
		when(usuarioGateway.existsByEmail(EMAIL)).thenReturn(false);

		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(true);

		when(passwordEncoderGateway.criptografar(SENHA)).thenReturn(SENHA_CRIPTOGRAFADA);

		useCase.executar(commandCliente);

		ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);

		verify(usuarioGateway).save(usuarioCaptor.capture());

		assertEquals(SENHA_CRIPTOGRAFADA, usuarioCaptor.getValue().getSenha());

		verify(passwordEncoderGateway).criptografar(SENHA);
	}

	@Test
	void deveLancarExcecaoQuandoEmailJaEstiverCadastrado() {
		when(usuarioGateway.existsByEmail(EMAIL)).thenReturn(true);

		UsuarioJaCadastradoException exception = assertThrows(UsuarioJaCadastradoException.class,
				() -> useCase.executar(commandCliente));

		assertEquals(AUTH_SERV_E_MAIL_JA_CADASTRADO, exception.getMessage());

		verify(usuarioGateway).existsByEmail(EMAIL);

		verify(usuarioGateway, never()).save(any(Usuario.class));

		verifyNoInteractions(clienteGateway, passwordEncoderGateway);

		verifyNoMoreInteractions(usuarioGateway);
	}

	@Test
	void deveLancarExcecaoQuandoRoleNaoForValida() {
		CriarUsuarioCommand command = new CriarUsuarioCommand(EMAIL, SENHA, DOCUMENTO, "SUPERVISOR");

		when(usuarioGateway.existsByEmail(EMAIL)).thenReturn(false);

		RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
				() -> useCase.executar(command));

		assertEquals("Role SUPERVISOR não é válida", exception.getMessage());

		verify(usuarioGateway).existsByEmail(EMAIL);

		verify(usuarioGateway, never()).save(any(Usuario.class));

		verifyNoInteractions(clienteGateway, passwordEncoderGateway);

		verifyNoMoreInteractions(usuarioGateway);
	}

	@Test
	void deveNormalizarRoleInvalidaAntesDeMontarMensagem() {
		CriarUsuarioCommand command = new CriarUsuarioCommand(EMAIL, SENHA, DOCUMENTO, "supervisor");

		when(usuarioGateway.existsByEmail(EMAIL)).thenReturn(false);

		RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
				() -> useCase.executar(command));

		assertEquals("Role SUPERVISOR não é válida", exception.getMessage());

		verify(usuarioGateway, never()).save(any(Usuario.class));

		verifyNoInteractions(clienteGateway, passwordEncoderGateway);
	}

	@Test
	void deveLancarExcecaoQuandoRoleClienteNaoEstiverCadastrado() {
		when(usuarioGateway.existsByEmail(EMAIL)).thenReturn(false);

		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(false);

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.executar(commandCliente));

		assertEquals(CLIENTE_NAO_ENCONTRADO, exception.getMessage());

		verify(usuarioGateway).existsByEmail(EMAIL);

		verify(clienteGateway).existsByDocumento(DOCUMENTO);

		verify(passwordEncoderGateway, never()).criptografar(any());

		verify(usuarioGateway, never()).save(any(Usuario.class));

		verifyNoMoreInteractions(usuarioGateway, clienteGateway, passwordEncoderGateway);
	}

	@Test
	void deveCriarUsuarioAdminSemValidarCliente() {
		deveCriarUsuarioNaoClienteSemValidarDocumento(ADMIN);
	}

	@Test
	void deveCriarUsuarioMecanicoSemValidarCliente() {
		deveCriarUsuarioNaoClienteSemValidarDocumento(MECANICO);
	}

	@Test
	void deveCriarUsuarioAtendenteSemValidarCliente() {
		deveCriarUsuarioNaoClienteSemValidarDocumento(ATENDENTE);
	}

	private void deveCriarUsuarioNaoClienteSemValidarDocumento(String role) {
		CriarUsuarioCommand command = new CriarUsuarioCommand(EMAIL, SENHA, DOCUMENTO, role);

		when(usuarioGateway.existsByEmail(EMAIL)).thenReturn(false);

		when(passwordEncoderGateway.criptografar(SENHA)).thenReturn(SENHA_CRIPTOGRAFADA);

		useCase.executar(command);

		ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);

		verify(usuarioGateway).save(usuarioCaptor.capture());

		Usuario usuarioSalvo = usuarioCaptor.getValue();

		assertAll(() -> assertEquals(EMAIL, usuarioSalvo.getEmail()),
				() -> assertEquals(SENHA_CRIPTOGRAFADA, usuarioSalvo.getSenha()),
				() -> assertEquals(role, usuarioSalvo.getRole()),
				() -> assertEquals(DOCUMENTO, usuarioSalvo.getDocumento()));

		verify(clienteGateway, never()).existsByDocumento(any());

		verify(passwordEncoderGateway).criptografar(SENHA);
	}
}