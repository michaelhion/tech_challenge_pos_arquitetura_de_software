package com.techchallenger.oficina360.frameworks.mappers.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.ReservaEstoqueDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.EstoqueEntity;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.ReservaEstoqueCommand;
import org.springframework.stereotype.Component;

@Component
public class EstoqueDTOMapper {


    public Estoque toDomain(EstoqueEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Estoque(
                entity.getId(),
                entity.getNome(),
                entity.getValor(),
                entity.getQuantidade(),
                entity.getReservados(),
                entity.getCodigo()
        );
    }

    public EstoqueEntity toEntity(Estoque domain) {
        if (domain == null) {
            return null;
        }

        return EstoqueEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .valor(domain.getValor())
                .quantidade(domain.getQuantidade())
                .reservados(domain.getReservados())
                .codigo(domain.getCodigo())
                .build();
    }


    public void updateEntityFromDomain(Estoque domain,EstoqueEntity entity) {
        entity.setNome(domain.getNome());
        entity.setValor(domain.getValor());
        entity.setQuantidade(domain.getQuantidade());
        entity.setReservados(domain.getReservados());
        entity.setCodigo(domain.getCodigo());
    }

    public static EstoqueDTO toDTO(EstoqueEntity estoqueEntity) {
        return new EstoqueDTO(
                estoqueEntity.getCodigo(),
                estoqueEntity.getNome(),
                estoqueEntity.getValor(),
                estoqueEntity.getQuantidade(),
                estoqueEntity.getReservados(),
                estoqueEntity.getDisponiveis()
        );
    }

    public static EstoqueDTO commandToDTO(EstoqueCommand command) {
        return new EstoqueDTO(
                command.codigo(),
                command.nome(),
                command.valor(),
                command.quantidade(),
                command.reservados(),
                command.disponiveis()
        );
    }

    public static EstoqueCommand dtoToCommand(EstoqueDTO dto) {
        return new EstoqueCommand(
                dto.codigo(),
                dto.nome(),
                dto.valor(),
                dto.quantidade(),
                dto.reservados(),
                dto.disponiveis()
        );
    }

    public static ReservaEstoqueCommand reservarDTOToCommand(ReservaEstoqueDTO dto){
        return new ReservaEstoqueCommand(dto.quantidade());
    }

}