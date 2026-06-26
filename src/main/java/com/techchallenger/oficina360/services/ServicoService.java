package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.entities.Servico;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.mappers.ServicoMapper;
import com.techchallenger.oficina360.repositories.ServicoRepository;
import com.techchallenger.oficina360.repositories.TempoExecucaoServicoRepository;
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
        Servico servico = toEntity(servicoDTO);
        servico.setTempoMedioExecucaoMinutos(0);
        Servico servicoSalvo = servicoRepository.save(servico);

        return toDTO(servicoSalvo);
    }

    public ServicoDTO edit(String codigo, ServicoDTO servicoDTO) {
        Servico servico = servicoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontradoException(SERVICO_NAO_ENCONTRADO));

        ServicoMapper.updateEntityFromDto(servicoDTO, servico);

        Servico servicoAtualizado = servicoRepository.save(servico);

        return toDTO(servicoAtualizado);
    }


    private ServicoDTO toDTOComTempoMedio(Servico servico) {

        Double media = tempoExecucaoServicoRepository
                .calcularTempoMedio(servico.getId());

        Integer tempoMedio = media != null
                ? (int) Math.round(media)
                : 0;

        return new ServicoDTO(
                servico.getCodigo(),
                servico.getDescricao(),
                servico.getValor(),
                tempoMedio
        );
    }

}