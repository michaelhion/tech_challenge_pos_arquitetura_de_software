package com.techchallenger.oficina360.it.fixtures.ordemservico;

import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoEstoqueDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;

import java.util.List;

import static com.techchallenger.oficina360.constants.Roles.ADMIN;
import static com.techchallenger.oficina360.constants.Roles.CLIENTE;

public final class OrdemServicoFixture {

    private OrdemServicoFixture() {
    }

    public static CriarOrdemServicoDTO ordemServicoValida() {

        return new CriarOrdemServicoDTO(null,"98765432100","DEF2G34","Barulho no motor",null);
    }

    public static CriarOrdemServicoDTO ordemServicoPlacaDeOutroCliente() {

        return new CriarOrdemServicoDTO(null,"98765432100","GHI3J45","Barulho no motor",null);
    }

    public static DiagnosticoDTO diagnosticoDTOValido(){
        return new DiagnosticoDTO(List.of("BALANCEAMENTO"),List.of(new DiagnosticoEstoqueDTO("PNEU-205-55-R16",4)));
    }

    public static AprovacaoOrdemServicoDTO aprovacaoOrdemServicoDTOValido(){
        return new AprovacaoOrdemServicoDTO(true,null);
    }

    public static UsuarioEntity gerarUsuarioAdmin() {
        return UsuarioEntity.builder().email("admin@oficina360.com").senha("123456").role(ADMIN).build();
    }

    public static UsuarioEntity gerarUsuarioCliente() {
        return UsuarioEntity.builder().email("cliente@oficina360.com").senha("123456").role(CLIENTE).build();
    }

    public static UsuarioEntity gerarUsuarioCliente1() {
        return UsuarioEntity.builder().email("cliente1@oficina360.com").senha("123456").role(CLIENTE).build();
    }

    public static UsuarioEntity gerarUsuarioCliente2() {
        return UsuarioEntity.builder().email("cliente2@oficina360.com").senha("123456").role(CLIENTE).build();
    }

}