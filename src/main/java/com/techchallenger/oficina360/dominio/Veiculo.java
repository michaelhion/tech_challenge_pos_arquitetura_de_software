package com.techchallenger.oficina360.dominio;

import java.util.UUID;

public class Veiculo {

    private UUID id;

    private String placa;

    private String marca;

    private String modelo;

    private String ano;

    private String clienteDocumento;

    public UUID getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getAno() {
        return ano;
    }

    public String getClienteDocumento() {
        return clienteDocumento;
    }

    public Veiculo(UUID id, String placa, String marca, String modelo, String ano, String clienteDocumento) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.clienteDocumento = clienteDocumento;
    }

    public Veiculo() {
    }

    public void atualizarVeiculo(Veiculo newVeiculo){
        this.placa = newVeiculo.getPlaca();
        this.marca= newVeiculo.getMarca();
        this.modelo= newVeiculo.getModelo();
        this.ano = newVeiculo.getAno();
        this.clienteDocumento = newVeiculo.getClienteDocumento();
    }
}