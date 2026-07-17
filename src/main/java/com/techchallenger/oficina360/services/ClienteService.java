package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.ClienteEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.ClienteRepository;
import com.techchallenger.oficina360.frameworks.web.exceptions.ConflitoException;
import com.techchallenger.oficina360.frameworks.web.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.mappers.ClienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_JA_CADASTRADO;
import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.mappers.ClienteMapper.toDTO;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;

@Service
@RequiredArgsConstructor
public class ClienteService {


    private final ClienteRepository clienteRepository;

    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream().map(ClienteMapper::toDTO).toList();
    }

    public void delete(String documento) {
        if(clienteRepository.existsByDocumento(documento)) {
            throw new RecursoNaoEncontradoException(CLIENTE_NAO_ENCONTRADO);
        }
        clienteRepository.deleteByDocumento(documento);
    }

    public ClienteDTO save(ClienteDTO clienteDTO) {
        if (clienteRepository.existsByDocumento(normalizarDocumento(clienteDTO.documento()))) {
            throw new ConflitoException(CLIENTE_JA_CADASTRADO);
        }
        return toDTO(clienteRepository.save(ClienteMapper.toEntity(clienteDTO)));
    }

    public Optional<ClienteDTO> findByDocumento(String documento) {
        return clienteRepository.findByDocumento(documento).map(ClienteMapper::toDTO);
    }

    public ClienteDTO edit(String documento, ClienteDTO dto) {
        ClienteEntity clienteEntity = clienteRepository.findByDocumento(documento)
                .orElseThrow(() -> new RecursoNaoEncontradoException(CLIENTE_NAO_ENCONTRADO));

        ClienteMapper.updateEntityFromDto(dto, clienteEntity);

        ClienteEntity clienteEntityAtualizado = clienteRepository.save(clienteEntity);

        return ClienteMapper.toDTO(clienteEntityAtualizado);
    }


}
