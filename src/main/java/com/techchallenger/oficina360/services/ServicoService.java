package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.ServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.ServicoRepository;
import com.techchallenger.oficina360.frameworks.persistence.repositories.TempoExecucaoServicoRepository;
import com.techchallenger.oficina360.frameworks.web.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.mappers.ServicoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.SERVICO_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.mappers.ServicoMapper.toDTO;
import static com.techchallenger.oficina360.mappers.ServicoMapper.toEntity;

@Service
@RequiredArgsConstructor
public class ServicoService {


    private final ServicoRepository servicoRepository;
    private final TempoExecucaoServicoRepository tempoExecucaoServicoRepository;


    public List<ServicoDTO> findAll() {

        return servicoRepository.findAll()
                .stream()
                .map(this::toDTOComTempoMedio)
                .toList();
    }


    public void delete(String codigo) {
       servicoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontradoException(SERVICO_NAO_ENCONTRADO));

        servicoRepository.deleteByCodigo(codigo);
    }

    public Optional<ServicoDTO> findByCodigo(String codigo) {
        return servicoRepository.findByCodigo(codigo)
                .map(ServicoMapper::toDTO);
    }

    public ServicoDTO save(ServicoDTO servicoDTO) {
        ServicoEntity servicoEntity = toEntity(servicoDTO);
        servicoEntity.setTempoMedioExecucaoMinutos(0);
        ServicoEntity servicoEntitySalvo = servicoRepository.save(servicoEntity);

        return toDTO(servicoEntitySalvo);
    }

    public ServicoDTO edit(String codigo, ServicoDTO servicoDTO) {
        ServicoEntity servicoEntity = servicoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontradoException(SERVICO_NAO_ENCONTRADO));

        ServicoMapper.updateEntityFromDto(servicoDTO, servicoEntity);

        ServicoEntity servicoEntityAtualizado = servicoRepository.save(servicoEntity);

        return toDTO(servicoEntityAtualizado);
    }


    private ServicoDTO toDTOComTempoMedio(ServicoEntity servicoEntity) {

        Double media = tempoExecucaoServicoRepository
                .calcularTempoMedio(servicoEntity.getId());

        Integer tempoMedio = media != null
                ? (int) Math.round(media)
                : 0;

        return new ServicoDTO(
                servicoEntity.getCodigo(),
                servicoEntity.getDescricao(),
                servicoEntity.getValor(),
                tempoMedio
        );
    }

}