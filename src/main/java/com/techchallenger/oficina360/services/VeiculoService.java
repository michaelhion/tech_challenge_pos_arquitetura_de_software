package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.entities.Veiculo;
import com.techchallenger.oficina360.exceptions.ConflitoException;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.mappers.VeiculoMapper;
import com.techchallenger.oficina360.repositories.ClienteRepository;
import com.techchallenger.oficina360.repositories.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.*;
import static com.techchallenger.oficina360.mappers.VeiculoMapper.toDTO;
import static com.techchallenger.oficina360.mappers.VeiculoMapper.toEntity;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    public List<VeiculoDTO> findAll() {
        return veiculoRepository.findAll()
                .stream()
                .map(VeiculoMapper::toDTO)
                .toList();
    }

    public Optional<VeiculoDTO> findByPlaca(String placa) {
        String placaNormalizada = normalizarPlaca(placa);

        return veiculoRepository.findByPlaca(placaNormalizada)
                .map(VeiculoMapper::toDTO);
    }

    @Transactional
    public VeiculoDTO save(VeiculoDTO veiculoDTO) {
        String placaNormalizada = normalizarPlaca(veiculoDTO.placa());
        String documentoClienteNormalizado = normalizarDocumento(veiculoDTO.clienteDocumento());

        validarClienteExiste(documentoClienteNormalizado);
        validarPlacaDisponivelParaCadastro(placaNormalizada);

        Veiculo veiculo = toEntity(veiculoDTO);
        veiculo.setPlaca(placaNormalizada);
        veiculo.setClienteDocumento(documentoClienteNormalizado);

        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);

        return toDTO(veiculoSalvo);
    }

    @Transactional
    public VeiculoDTO edit(String placaAtual, VeiculoDTO veiculoDTO) {
        String placaAtualNormalizada = normalizarPlaca(placaAtual);
        String novaPlacaNormalizada = normalizarPlaca(veiculoDTO.placa());
        String documentoClienteNormalizado = normalizarDocumento(veiculoDTO.clienteDocumento());

        Veiculo veiculo = buscarVeiculoPorPlaca(placaAtualNormalizada);

        validarClienteExiste(documentoClienteNormalizado);
        validarPlacaDisponivelParaEdicao(novaPlacaNormalizada, veiculo);

        VeiculoMapper.updateEntityFromDto(veiculoDTO, veiculo);

        veiculo.setPlaca(novaPlacaNormalizada);
        veiculo.setClienteDocumento(documentoClienteNormalizado);

        Veiculo veiculoAtualizado = veiculoRepository.save(veiculo);

        return toDTO(veiculoAtualizado);
    }

    @Transactional
    public void delete(String placa) {
        String placaNormalizada = normalizarPlaca(placa);

        validarVeiculoExistePorPlaca(placaNormalizada);

        veiculoRepository.deleteByPlaca(placaNormalizada);
    }

    private Veiculo buscarVeiculoPorPlaca(String placa) {
        return veiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new RecursoNaoEncontradoException(VEICULO_SERV_VEICULO_NAO_ENCONTRADO));
    }

    private void validarClienteExiste(String documentoCliente) {
        if (!clienteRepository.existsByDocumento(documentoCliente)) {
            throw new RecursoNaoEncontradoException(VEICULO_SERV_CLIENTE_NAO_ENCONTRADO);
        }
    }

    private void validarVeiculoExistePorPlaca(String placa) {
        if (!veiculoRepository.existsByPlaca(placa)) {
            throw new RecursoNaoEncontradoException(VEICULO_SERV_VEICULO_NAO_ENCONTRADO);
        }
    }

    private void validarPlacaDisponivelParaCadastro(String placa) {
        if (veiculoRepository.existsByPlaca(placa)) {
            throw new ConflitoException(VEICULO_SERV_VEICULO_CADASTRADO);
        }
    }

    private void validarPlacaDisponivelParaEdicao(
            String novaPlaca,
            Veiculo veiculoAtual
    ) {
        if (veiculoRepository.existsByPlacaAndIdNot(novaPlaca, veiculoAtual.getId())) {
            throw new ConflitoException(VEICULO_SERV_VEICULO_CADASTRADO);
        }
    }
}