package com.techchallenger.oficina360.usecases.autorizacao;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.gateways.UsuarioAutenticadoGateway;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutorizacaoClienteUseCaseTest {

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	private static final String DOCUMENTO_OUTRO_CLIENTE = "98765432100";

	private static final String PLACA = "ABC1D23";

	@Mock
	private VeiculoGateway veiculoGateway;

	@Mock
	private OrdemServicoGateway ordemServicoGateway;

	@Mock
	private UsuarioAutenticadoGateway usuarioAutenticadoGateway;

	@Mock
	private Usuario usuario;

	@Mock
	private Veiculo veiculo;

	@Mock
	private OrdemServico ordemServico;

	@InjectMocks
	private AutorizacaoClienteUseCase useCase;

	@Test
	void devePermitirAcessoAoProprioClientePorDocumento() {
		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(usuario);

		when(usuario.getDocumento()).thenReturn(DOCUMENTO_CLIENTE);

		boolean resultado = useCase.podeAcessarClientePorDocumento(DOCUMENTO_CLIENTE);

		assertTrue(resultado);

		verify(usuarioAutenticadoGateway).obterUsuarioAtual();

		verify(usuario, times(3)).getDocumento();

		verifyNoInteractions(veiculoGateway, ordemServicoGateway);
	}

	@Test
	void deveNegarAcessoAoClienteDeOutroDocumento() {
		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(usuario);

		when(usuario.getDocumento()).thenReturn(DOCUMENTO_CLIENTE);

		boolean resultado = useCase.podeAcessarClientePorDocumento(DOCUMENTO_OUTRO_CLIENTE);

		assertFalse(resultado);

		verify(usuarioAutenticadoGateway).obterUsuarioAtual();

		verify(usuario, times(3)).getDocumento();

		verifyNoInteractions(veiculoGateway, ordemServicoGateway);
	}

	@Test
	void devePermitirAcessoAoVeiculoDoUsuarioAutenticado() {
		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(usuario);

		when(usuario.getDocumento()).thenReturn(DOCUMENTO_CLIENTE);

		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.of(veiculo));

		when(veiculo.getClienteDocumento()).thenReturn(DOCUMENTO_CLIENTE);

		boolean resultado = useCase.podeAcessarVeiculo(PLACA);

		assertTrue(resultado);

		verify(usuarioAutenticadoGateway).obterUsuarioAtual();

		verify(veiculoGateway).findByPlaca(PLACA);

		verify(veiculo).getClienteDocumento();

		verifyNoInteractions(ordemServicoGateway);
	}

	@Test
	void deveNegarAcessoAoVeiculoDeOutroCliente() {
		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(usuario);

		when(usuario.getDocumento()).thenReturn(DOCUMENTO_CLIENTE);

		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.of(veiculo));

		when(veiculo.getClienteDocumento()).thenReturn(DOCUMENTO_OUTRO_CLIENTE);

		boolean resultado = useCase.podeAcessarVeiculo(PLACA);

		assertFalse(resultado);

		verify(usuarioAutenticadoGateway).obterUsuarioAtual();

		verify(veiculoGateway).findByPlaca(PLACA);

		verify(veiculo).getClienteDocumento();

		verifyNoInteractions(ordemServicoGateway);
	}

	@Test
	void deveNegarAcessoQuandoVeiculoNaoExistir() {
		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(usuario);

		when(usuario.getDocumento()).thenReturn(DOCUMENTO_CLIENTE);

		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.empty());

		boolean resultado = useCase.podeAcessarVeiculo(PLACA);

		assertFalse(resultado);

		verify(usuarioAutenticadoGateway).obterUsuarioAtual();

		verify(veiculoGateway).findByPlaca(PLACA);

		verifyNoInteractions(ordemServicoGateway, veiculo);
	}

	@Test
	void devePermitirAcessoAOrdemServicoDoUsuarioAutenticado() {
		UUID ordemServicoId = UUID.randomUUID();

		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(usuario);

		when(usuario.getDocumento()).thenReturn(DOCUMENTO_CLIENTE);

		when(ordemServicoGateway.findById(ordemServicoId)).thenReturn(Optional.of(ordemServico));

		when(ordemServico.getDocumentoCliente()).thenReturn(DOCUMENTO_CLIENTE);

		boolean resultado = useCase.podeAcessarOrdemServico(ordemServicoId);

		assertTrue(resultado);

		verify(usuarioAutenticadoGateway).obterUsuarioAtual();

		verify(ordemServicoGateway).findById(ordemServicoId);

		verify(ordemServico).getDocumentoCliente();

		verifyNoInteractions(veiculoGateway);
	}

	@Test
	void deveNegarAcessoAOrdemServicoDeOutroCliente() {
		UUID ordemServicoId = UUID.randomUUID();

		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(usuario);

		when(usuario.getDocumento()).thenReturn(DOCUMENTO_CLIENTE);

		when(ordemServicoGateway.findById(ordemServicoId)).thenReturn(Optional.of(ordemServico));

		when(ordemServico.getDocumentoCliente()).thenReturn(DOCUMENTO_OUTRO_CLIENTE);

		boolean resultado = useCase.podeAcessarOrdemServico(ordemServicoId);

		assertFalse(resultado);

		verify(usuarioAutenticadoGateway).obterUsuarioAtual();

		verify(ordemServicoGateway).findById(ordemServicoId);

		verify(ordemServico).getDocumentoCliente();

		verifyNoInteractions(veiculoGateway);
	}

	@Test
	void deveNegarAcessoQuandoOrdemServicoNaoExistir() {
		UUID ordemServicoId = UUID.randomUUID();

		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(usuario);

		when(usuario.getDocumento()).thenReturn(DOCUMENTO_CLIENTE);

		when(ordemServicoGateway.findById(ordemServicoId)).thenReturn(Optional.empty());

		boolean resultado = useCase.podeAcessarOrdemServico(ordemServicoId);

		assertFalse(resultado);

		verify(usuarioAutenticadoGateway).obterUsuarioAtual();

		verify(ordemServicoGateway).findById(ordemServicoId);

		verifyNoInteractions(veiculoGateway, ordemServico);
	}

	@Test
	void deveNegarAcessoQuandoNaoHouverUsuarioAutenticado() {
		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(null);

		boolean acessoCliente = useCase.podeAcessarClientePorDocumento(DOCUMENTO_CLIENTE);

		boolean acessoVeiculo = useCase.podeAcessarVeiculo(PLACA);

		boolean acessoOrdemServico = useCase.podeAcessarOrdemServico(UUID.randomUUID());

		assertFalse(acessoCliente);
		assertFalse(acessoVeiculo);
		assertFalse(acessoOrdemServico);

		verifyNoInteractions(veiculoGateway, ordemServicoGateway);
	}

	@Test
	void deveNegarAcessoQuandoUsuarioNaoPossuirDocumento() {
		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(usuario);

		when(usuario.getDocumento()).thenReturn(null);

		boolean acessoCliente = useCase.podeAcessarClientePorDocumento(DOCUMENTO_CLIENTE);

		boolean acessoVeiculo = useCase.podeAcessarVeiculo(PLACA);

		boolean acessoOrdemServico = useCase.podeAcessarOrdemServico(UUID.randomUUID());

		assertFalse(acessoCliente);
		assertFalse(acessoVeiculo);
		assertFalse(acessoOrdemServico);

		verifyNoInteractions(veiculoGateway, ordemServicoGateway);
	}

	@Test
	void deveNegarAcessoQuandoDocumentoDoUsuarioEstiverEmBranco() {
		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(usuario);

		when(usuario.getDocumento()).thenReturn("   ");

		boolean acessoCliente = useCase.podeAcessarClientePorDocumento(DOCUMENTO_CLIENTE);

		boolean acessoVeiculo = useCase.podeAcessarVeiculo(PLACA);

		boolean acessoOrdemServico = useCase.podeAcessarOrdemServico(UUID.randomUUID());

		assertFalse(acessoCliente);
		assertFalse(acessoVeiculo);
		assertFalse(acessoOrdemServico);

		verifyNoInteractions(veiculoGateway, ordemServicoGateway);
	}

	@Test
	void deveNormalizarPlacaAntesDeBuscarVeiculo() {
		String placaInformada = " abc1d23 ";

		when(usuarioAutenticadoGateway.obterUsuarioAtual()).thenReturn(usuario);

		when(usuario.getDocumento()).thenReturn(DOCUMENTO_CLIENTE);

		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.of(veiculo));

		when(veiculo.getClienteDocumento()).thenReturn(DOCUMENTO_CLIENTE);

		boolean resultado = useCase.podeAcessarVeiculo(placaInformada);

		assertTrue(resultado);

		verify(veiculoGateway).findByPlaca(PLACA);
	}

}