package com.techchallenger.oficina360.constants;

public interface MensagensDeErroConstant {

    //ORDEM DE SERVIÇO OFICINA
    String OS_ORDEM_DE_SERVICO_NAO_ENCONTRADA = "Ordem de servi\u00E7o n\u00E3o encontrada";
    String OS_CLIENTE_NAO_ENCONTRADO = "Cliente n\u00E3o encontrado";
    String OS_VEICULO_NAO_ENCONTRADO = "Ve\u00EDculo n\u00E3o encontrado";
    String OS_VEICULO_NAO_PERTENCE_AO_CLIENTE = "O ve\u00EDculo n\u00E3o pertence ao cliente informado";
    String OS_ORDEM_DE_SERVICO_ATIVA_PARA_O_VEICULO = "J\u00E1 existe uma ordem de servi\u00E7o ativa para o ve\u00EDculo de placa %s. OS ativa: %s, status: %s";
    String OS_NAO_PERTENCE_AO_USER = "Usu\u00E1rio autenticado n\u00E3o \u00E9 o cliente dono da ordem de servi\u00E7o";

    //ORDEM DE SERVIÇO CLIENTE
    String OS_CLIENTE_DOCUMENTO_DIFERENTE_USER_LOGADO = "Documento informado n\u00E3o pertence ao usu\u00E1rio logado";

    //VEICULO
    String VEICULO_SERV_VEICULO_NAO_ENCONTRADO = "Veículo não encontrado";
    String VEICULO_SERV_CLIENTE_NAO_ENCONTRADO = "Cliente não encontrado para o documento informado";
    String VEICULO_SERV_VEICULO_CADASTRADO = "Já existe veículo cadastrado com essa placa";

    //ESTOQUE
    String ESTOQUE_ITEM_DE_ESTOQUE_NAO_DISPONIVEL = "Item de estoque n\u00E3o dispon\u00EDvel";
    String ESTOQUE_CODIGO_JA_EXISTE_NO_SISTEMA = "C\u00F3digo j\u00E1 existe no sistema";
    String ESTOQUE_ITEM_NAO_ENCONTRADO = "Item de estoque não encontrado";

    //CLIENTE
    String CLIENTE_NAO_ENCONTRADO = "Cliente n\u00E3o encontrado";
    String CLIENTE_JA_CADASTRADO = "Cliente já cadastrado";

    //SERVICO
    String SERVICO_NAO_ENCONTRADO = "Servi\u00E7o n\u00E3o encontrado";
}
