package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.entities.OrdemServico;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.mappers.OrdemServicoMapper;
import com.techchallenger.oficina360.repositories.OrdemServicosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.OS_ORDEM_DE_SERVICO_NAO_ENCONTRADA;
import static com.techchallenger.oficina360.mappers.OrdemServicoMapper.toDTO;

@Service
@RequiredArgsConstructor
public class OrdemServicoClienteService {

    private final OrdemServicosRepository ordemServicosRepository;

    public Optional<OrdemServicoDTO> findById(UUID id) {
        return ordemServicosRepository.findById(id)
                .map(OrdemServicoMapper::toDTO);
    }

    public OrdemServicoDTO aprovar(UUID id, AprovacaoOrdemServicoDTO aprovacaoDTO) {
        OrdemServico ordemServico =ordemServicosRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(OS_ORDEM_DE_SERVICO_NAO_ENCONTRADA));

        ordemServico.registrarAprovacao(aprovacaoDTO.aprovado());
        if (aprovacaoDTO.observacao() != null && !aprovacaoDTO.observacao().isBlank()) {
            ordemServico.setObservacaoCliente(aprovacaoDTO.observacao());
        }
        OrdemServico ordemServicoAtualizada = ordemServicosRepository.save(ordemServico);

        return toDTO(ordemServicoAtualizada);
    }
}
