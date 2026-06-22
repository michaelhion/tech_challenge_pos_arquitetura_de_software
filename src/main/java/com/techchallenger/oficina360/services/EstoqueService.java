package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.ReservaEstoqueDTO;
import com.techchallenger.oficina360.entities.Estoque;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.exceptions.RegraDeNegocioException;
import com.techchallenger.oficina360.mappers.EstoqueMapper;
import com.techchallenger.oficina360.repositories.EstoqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.*;
import static com.techchallenger.oficina360.mappers.EstoqueMapper.toDTO;
import static com.techchallenger.oficina360.mappers.EstoqueMapper.toEntity;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;

    public List<EstoqueDTO> findAll() {
        return estoqueRepository.findAll().stream().map(EstoqueMapper::toDTO).toList();
    }

    public void delete(UUID id) {
        estoqueRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(ESTOQUE_ITEM_DE_ESTOQUE_NAO_DISPONIVEL));
        estoqueRepository.deleteById(id);
    }

    public Optional<EstoqueDTO> findByCodigo(String codigo) {
        return estoqueRepository.findByCodigo(codigo).map(EstoqueMapper::toDTO);
    }

    public EstoqueDTO save(EstoqueDTO estoqueDTO) {
        estoqueRepository.findByCodigo(estoqueDTO.codigo())
                .ifPresent(existing -> {
                    throw new RegraDeNegocioException(ESTOQUE_CODIGO_JA_EXISTE_NO_SISTEMA);
                });
        return toDTO(estoqueRepository.save(toEntity(estoqueDTO)));
    }


    public EstoqueDTO edit(UUID id, EstoqueDTO estoqueDTO) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(ESTOQUE_ITEM_DE_ESTOQUE_NAO_DISPONIVEL));

        EstoqueMapper.updateEntityFromDto(estoqueDTO, estoque);

        Estoque estoqueAtualizado = estoqueRepository.save(estoque);

        return toDTO(estoqueAtualizado);
    }


    public EstoqueDTO reservar(UUID id, ReservaEstoqueDTO reservaDTO) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(ESTOQUE_ITEM_NAO_ENCONTRADO));

        estoque.reservar(reservaDTO.quantidade());

        Estoque estoqueAtualizado = estoqueRepository.save(estoque);

        return EstoqueMapper.toDTO(estoqueAtualizado);
    }
}
