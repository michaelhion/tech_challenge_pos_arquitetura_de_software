package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.entities.Servico;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.mappers.ServicoMapper;
import com.techchallenger.oficina360.repositories.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.techchallenger.oficina360.mappers.ServicoMapper.toDTO;
import static com.techchallenger.oficina360.mappers.ServicoMapper.toEntity;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private static final String SERVICO_NAO_ENCONTRADO = "Servi\u00E7o n\u00E3o encontrado";

    private final ServicoRepository servicoRepository;

    public List<ServicoDTO> findAll() {
        return servicoRepository.findAll()
                .stream()
                .map(ServicoMapper::toDTO)
                .toList();
    }

    public void delete(UUID id) {
       servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(SERVICO_NAO_ENCONTRADO));

        servicoRepository.deleteById(id);
    }

    public Optional<ServicoDTO> findById(String codigo) {
        return servicoRepository.findByCodigo(codigo)
                .map(ServicoMapper::toDTO);
    }

    public ServicoDTO save(ServicoDTO servicoDTO) {
        Servico servico = toEntity(servicoDTO);
        Servico servicoSalvo = servicoRepository.save(servico);

        return toDTO(servicoSalvo);
    }

    public ServicoDTO edit(UUID id, ServicoDTO servicoDTO) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(SERVICO_NAO_ENCONTRADO));

        ServicoMapper.updateEntityFromDto(servicoDTO, servico);

        Servico servicoAtualizado = servicoRepository.save(servico);

        return toDTO(servicoAtualizado);
    }
}