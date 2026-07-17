package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.constants.MensagensDeErroConstant;
import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.VeiculoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.ClienteRepository;
import com.techchallenger.oficina360.frameworks.persistence.repositories.VeiculoRepository;
import com.techchallenger.oficina360.frameworks.web.exceptions.ConflitoException;
import com.techchallenger.oficina360.frameworks.web.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.mappers.VeiculoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.VEICULO_SERV_CLIENTE_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.VEICULO_SERV_VEICULO_CADASTRADO;
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

        VeiculoEntity veiculoEntity = toEntity(veiculoDTO);
        veiculoEntity.setPlaca(placaNormalizada);
        veiculoEntity.setClienteDocumento(documentoClienteNormalizado);

        VeiculoEntity veiculoEntitySalvo = veiculoRepository.save(veiculoEntity);

        return toDTO(veiculoEntitySalvo);
    }

    @Transactional
    public VeiculoDTO edit(String placaAtual, VeiculoDTO veiculoDTO) {
        String placaAtualNormalizada = normalizarPlaca(placaAtual);
        String novaPlacaNormalizada = normalizarPlaca(veiculoDTO.placa());
        String documentoClienteNormalizado = normalizarDocumento(veiculoDTO.clienteDocumento());

        VeiculoEntity veiculoEntity = buscarVeiculoPorPlaca(placaAtualNormalizada);

        validarClienteExiste(documentoClienteNormalizado);
        validarPlacaDisponivelParaEdicao(novaPlacaNormalizada, veiculoEntity);

        VeiculoMapper.updateEntityFromDto(veiculoDTO, veiculoEntity);

        veiculoEntity.setPlaca(novaPlacaNormalizada);
        veiculoEntity.setClienteDocumento(documentoClienteNormalizado);

        VeiculoEntity veiculoEntityAtualizado = veiculoRepository.save(veiculoEntity);

        return toDTO(veiculoEntityAtualizado);
    }

    @Transactional
    public void delete(String placa) {
        String placaNormalizada = normalizarPlaca(placa);

        validarVeiculoExistePorPlaca(placaNormalizada);

        veiculoRepository.deleteByPlaca(placaNormalizada);
    }

    private VeiculoEntity buscarVeiculoPorPlaca(String placa) {
        return veiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new RecursoNaoEncontradoException(MensagensDeErroConstant.VEICULO_NAO_ENCONTRADO));
    }

    private void validarClienteExiste(String documentoCliente) {
        if (!clienteRepository.existsByDocumento(documentoCliente)) {
            throw new RecursoNaoEncontradoException(VEICULO_SERV_CLIENTE_NAO_ENCONTRADO);
        }
    }

    private void validarVeiculoExistePorPlaca(String placa) {
        if (!veiculoRepository.existsByPlaca(placa)) {
            throw new RecursoNaoEncontradoException(MensagensDeErroConstant.VEICULO_NAO_ENCONTRADO);
        }
    }

    private void validarPlacaDisponivelParaCadastro(String placa) {
        if (veiculoRepository.existsByPlaca(placa)) {
            throw new ConflitoException(VEICULO_SERV_VEICULO_CADASTRADO);
        }
    }

    private void validarPlacaDisponivelParaEdicao(
            String novaPlaca,
            VeiculoEntity veiculoEntityAtual
    ) {
        if (veiculoRepository.existsByPlacaAndIdNot(novaPlaca, veiculoEntityAtual.getId())) {
            throw new ConflitoException(VEICULO_SERV_VEICULO_CADASTRADO);
        }
    }
}