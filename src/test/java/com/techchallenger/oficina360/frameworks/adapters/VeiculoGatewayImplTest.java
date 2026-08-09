package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.frameworks.mappers.veiculo.VeiculoDTOMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.VeiculoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoGatewayImplTest {

	private static final UUID VEICULO_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final UUID OUTRO_VEICULO_ID = UUID.fromString("3c4efe7e-3f54-5a3f-9fb4-37825c2409f9");

	private static final String PLACA = "ABC1D23";

	@Mock
	private VeiculoDTOMapper veiculoDTOMapper;

	@Mock
	private VeiculoRepository repository;

	@Mock
	private Veiculo veiculo;

	@Mock
	private Veiculo outroVeiculo;

	@Mock
	private VeiculoEntity veiculoEntity;

	@Mock
	private VeiculoEntity outroVeiculoEntity;

	@Mock
	private VeiculoEntity veiculoEntityPersistido;

	@InjectMocks
	private VeiculoGatewayImpl gateway;

	@Test
	void deveBuscarVeiculoPorPlacaComSucesso() {
		when(repository.findByPlaca(PLACA)).thenReturn(Optional.of(veiculoEntity));

		when(veiculoDTOMapper.toDomain(veiculoEntity)).thenReturn(veiculo);

		Optional<Veiculo> resultado = gateway.findByPlaca(PLACA);

		assertTrue(resultado.isPresent());

		assertSame(veiculo, resultado.orElseThrow());

		verify(repository).findByPlaca(PLACA);

		verify(veiculoDTOMapper).toDomain(veiculoEntity);
	}

	@Test
	void deveRetornarOptionalVazioQuandoVeiculoNaoExistirPorPlaca() {
		when(repository.findByPlaca(PLACA)).thenReturn(Optional.empty());

		Optional<Veiculo> resultado = gateway.findByPlaca(PLACA);

		assertTrue(resultado.isEmpty());

		verify(repository).findByPlaca(PLACA);

		verify(veiculoDTOMapper, never()).toDomain(veiculoEntity);
	}

	@Test
	void deveVerificarExistenciaPorPlaca() {
		when(repository.existsByPlaca(PLACA)).thenReturn(true);

		boolean resultado = gateway.existsByPlaca(PLACA);

		assertTrue(resultado);

		verify(repository).existsByPlaca(PLACA);
	}

	@Test
	void deveRetornarFalseQuandoPlacaNaoExistir() {
		when(repository.existsByPlaca(PLACA)).thenReturn(false);

		boolean resultado = gateway.existsByPlaca(PLACA);

		assertFalse(resultado);

		verify(repository).existsByPlaca(PLACA);
	}

	@Test
	void deveVerificarSePlacaPertenceAOutroVeiculo() {
		when(repository.existsByPlacaAndIdNot(PLACA, VEICULO_ID)).thenReturn(true);

		boolean resultado = gateway.existsByPlacaAndIdNot(PLACA, VEICULO_ID);

		assertTrue(resultado);

		verify(repository).existsByPlacaAndIdNot(PLACA, VEICULO_ID);
	}

	@Test
	void deveRetornarFalseQuandoPlacaNaoPertencerAOutroVeiculo() {
		when(repository.existsByPlacaAndIdNot(PLACA, VEICULO_ID)).thenReturn(false);

		boolean resultado = gateway.existsByPlacaAndIdNot(PLACA, VEICULO_ID);

		assertFalse(resultado);

		verify(repository).existsByPlacaAndIdNot(PLACA, VEICULO_ID);
	}

	@Test
	void deveExcluirVeiculoPorPlaca() {
		gateway.deleteByPlaca(PLACA);

		verify(repository).deleteByPlaca(PLACA);
	}

	@Test
	void deveSalvarVeiculoComSucesso() {
		when(veiculoDTOMapper.toEntity(veiculo)).thenReturn(veiculoEntity);

		when(repository.save(veiculoEntity)).thenReturn(veiculoEntityPersistido);

		when(veiculoDTOMapper.toDomain(veiculoEntityPersistido)).thenReturn(veiculo);

		Veiculo resultado = gateway.save(veiculo);

		assertSame(veiculo, resultado);

		verify(veiculoDTOMapper).toEntity(veiculo);

		verify(repository).save(veiculoEntity);

		verify(veiculoDTOMapper).toDomain(veiculoEntityPersistido);
	}

	@Test
	void deveConverterPersistirEConverterNovamenteAoSalvar() {
		when(veiculoDTOMapper.toEntity(veiculo)).thenReturn(veiculoEntity);

		when(repository.save(veiculoEntity)).thenReturn(veiculoEntityPersistido);

		when(veiculoDTOMapper.toDomain(veiculoEntityPersistido)).thenReturn(veiculo);

		gateway.save(veiculo);

		InOrder ordemDasChamadas = inOrder(veiculoDTOMapper, repository);

		ordemDasChamadas.verify(veiculoDTOMapper).toEntity(veiculo);

		ordemDasChamadas.verify(repository).save(veiculoEntity);

		ordemDasChamadas.verify(veiculoDTOMapper).toDomain(veiculoEntityPersistido);

		ordemDasChamadas.verifyNoMoreInteractions();
	}

	@Test
	void deveListarTodosOsVeiculos() {
		when(repository.findAll()).thenReturn(List.of(veiculoEntity, outroVeiculoEntity));

		when(veiculoDTOMapper.toDomain(veiculoEntity)).thenReturn(veiculo);

		when(veiculoDTOMapper.toDomain(outroVeiculoEntity)).thenReturn(outroVeiculo);

		List<Veiculo> resultado = gateway.findAll();

		assertFalse(resultado.isEmpty());

		assertSame(veiculo, resultado.get(0));

		assertSame(outroVeiculo, resultado.get(1));

		verify(repository).findAll();

		verify(veiculoDTOMapper).toDomain(veiculoEntity);

		verify(veiculoDTOMapper).toDomain(outroVeiculoEntity);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremVeiculos() {
		when(repository.findAll()).thenReturn(List.of());

		List<Veiculo> resultado = gateway.findAll();

		assertTrue(resultado.isEmpty());

		verify(repository).findAll();

		verify(veiculoDTOMapper, never()).toDomain(veiculoEntity);
	}

	@Test
	void deveBuscarVeiculoPorIdComSucesso() {
		when(repository.findById(VEICULO_ID)).thenReturn(Optional.of(veiculoEntity));

		when(veiculoDTOMapper.toDomain(veiculoEntity)).thenReturn(veiculo);

		Optional<Veiculo> resultado = gateway.findById(VEICULO_ID);

		assertTrue(resultado.isPresent());

		assertSame(veiculo, resultado.orElseThrow());

		verify(repository).findById(VEICULO_ID);

		verify(veiculoDTOMapper).toDomain(veiculoEntity);
	}

	@Test
	void deveRetornarOptionalVazioQuandoVeiculoNaoExistirPorId() {
		when(repository.findById(OUTRO_VEICULO_ID)).thenReturn(Optional.empty());

		Optional<Veiculo> resultado = gateway.findById(OUTRO_VEICULO_ID);

		assertTrue(resultado.isEmpty());

		verify(repository).findById(OUTRO_VEICULO_ID);

		verify(veiculoDTOMapper, never()).toDomain(veiculoEntity);
	}
}