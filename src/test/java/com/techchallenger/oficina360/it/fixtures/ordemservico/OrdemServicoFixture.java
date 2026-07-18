package com.techchallenger.oficina360.it.fixtures.ordemservico;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoEstoqueDTO;

import java.util.List;
import java.util.UUID;

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

    public static Usuario gerarUsuarioAdmin() {
        return new Usuario(
                UUID.fromString("99999999-9999-9999-9999-999999999998"),
                "admin@oficina360.com",
                "123456",
                ADMIN,
                "12345678801");
    }

    public static Usuario gerarUsuarioCliente() {

        return new Usuario(
                UUID.fromString("99999999-9999-9999-9999-999999999998"),
                "cliente@oficina360.com",
                "123456",
                CLIENTE,
                "12345678901");
    }

    public static Usuario gerarUsuarioCliente1() {
        return new Usuario(
                UUID.fromString("99999999-9999-9999-9999-999999999977"),
                "cliente1@oficina360.com",
                "123456",
                CLIENTE,
                "12345678801");

    }

    public static Usuario gerarUsuarioCliente2() {
        return new Usuario(
                UUID.fromString("99999999-9999-9999-9999-999999998877"),
                "cliente2@oficina360.com",
                "123456",
                CLIENTE,
                "98765432100");
    }

}