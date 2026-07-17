package com.techchallenger.oficina360.constants;


public final class MensagensDeErroConstant {

    private MensagensDeErroConstant() {
    }

    public static final String AMERICA_SAO_PAULO = "America/Sao_Paulo";

    //ORDEM DE SERVIÇO OFICINA
    public static final String OS_ORDEM_DE_SERVICO_NAO_ENCONTRADA = "Ordem de servi\u00E7o n\u00E3o encontrada";
    public static final String VEICULO_NAO_ENCONTRADO = "Ve\u00EDculo n\u00E3o encontrado";
    public static final String OS_VEICULO_NAO_PERTENCE_AO_CLIENTE = "O ve\u00EDculo n\u00E3o pertence ao cliente informado";
    public static final String OS_ORDEM_DE_SERVICO_ATIVA_PARA_O_VEICULO = "J\u00E1 existe uma ordem de servi\u00E7o ativa para o ve\u00EDculo de placa %s. OS ativa: %s, status: %s";
    public static final String OS_NAO_PERTENCE_AO_USER = "Usu\u00E1rio autenticado n\u00E3o \u00E9 o cliente dono da ordem de servi\u00E7o";

    //ORDEM DE SERVIÇO CLIENTE
    public static final String OS_CLIENTE_DOCUMENTO_DIFERENTE_USER_LOGADO = "Documento informado n\u00E3o pertence ao usu\u00E1rio logado";

    //VEICULO
    public static final String VEICULO_SERV_CLIENTE_NAO_ENCONTRADO = "Cliente n\u00E3o encontrado para o documento informado";
    public static final String VEICULO_SERV_VEICULO_CADASTRADO = "J\u00E1 existe ve\u00EDculo cadastrado com essa placa";

    //ESTOQUE
    public static final String ESTOQUE_ITEM_DE_ESTOQUE_NAO_DISPONIVEL = "Item de estoque n\u00E3o dispon\u00EDvel";
    public static final String ESTOQUE_CODIGO_JA_EXISTE_NO_SISTEMA = "C\u00F3digo j\u00E1 existe no sistema";
    public static final String ESTOQUE_ITEM_NAO_ENCONTRADO = "Item de estoque não encontrado";

    //CLIENTE
    public static final String CLIENTE_NAO_ENCONTRADO = "Cliente n\u00E3o encontrado";
    public static final String CLIENTE_JA_CADASTRADO = "Cliente j\u00E1 cadastrado";

    //SERVICO
    public static final String SERVICO_NAO_ENCONTRADO = "Servi\u00E7o n\u00E3o encontrado";

    //OS_ENTITY

    public static final String OS_ENTITY_A_ORDEM_DE_SERVICO_NAO_PERMITE_INICIO_DE_DIAGNOSTICO_NO_STATUS_ATUAL = "A ordem de servi\u00E7o n\u00E3o permite in\u00EDcio de diagn\u00F3stico no status atual";
    public static final String OS_ENTITY_SERVICO_DA_ORDEM_DE_SERVICO_E_OBRIGATORIO = "Servi\u00E7o da ordem de servi\u00E7o \u00E9 obrigat\u00F3rio";
    public static final String OS_ENTITY_ITEM_DE_ESTOQUE_DA_ORDEM_DE_SERVICO_E_OBRIGATORIO = "Item de estoque da ordem de servi\u00E7o \u00E9 obrigat\u00F3rio";
    public static final String OS_ENTITY_A_ORDEM_DE_SERVICO_NAO_ESTA_EM_DIAGNOSTICO = "A ordem de servi\u00E7o n\u00E3o est\u00E1 em diagn\u00F3stico";
    public static final String OS_ENTITY_A_ORDEM_DE_SERVICO_PRECISA_TER_AO_MENOS_UM_SERVICO_DEFINIDO = "A ordem de servi\u00E7o precisa ter ao menos um servi\u00E7o definido";
    public static final String OS_ENTITY_INFORME_SE_O_ORCAMENTO_FOI_APROVADO_OU_REPROVADO = "Informe se o or\u00E7amento foi aprovado ou reprovado";
    public static final String OS_ENTITY_A_ORDEM_DE_SERVICO_NAO_ESTA_AGUARDANDO_APROVACAO = "A ordem de servi\u00E7o n\u00E3o est\u00E1 aguardando aprova\u00E7\u00E3o";
    public static final String OS_ENTITY_A_EXECUCAO_SO_PODE_SER_INICIADA_APOS_APROVACAO_DO_ORCAMENTO = "A execu\u00E7\u00E3o s\u00F3 pode ser iniciada ap\u00F3s aprova\u00E7\u00E3o do or\u00E7amento";
    public static final String OS_ENTITY_A_EXECUCAO_SO_PODE_SER_FINALIZADA_QUANDO_ESTIVER_EM_EXECUCAO = "A execu\u00E7\u00E3o s\u00F3 pode ser finalizada quando estiver em execu\u00E7\u00E3o";
    public static final String OS_ENTITY_A_EXECUCAO_NAO_POSSUI_DATA_HORA_DE_INICIO_REGISTRADA = "A execu\u00E7\u00E3o n\u00E3o possui data/hora de in\u00EDcio registrada";
    public static final String OS_ENTITY_A_ORDEM_DE_SERVICO_SO_PODE_SER_ENTREGUE_APOS_FINALIZACAO = "A ordem de servi\u00E7o s\u00F3 pode ser entregue ap\u00F3s finaliza\u00E7\u00E3o";

    //ESTOQUE_ENTITY
    public static final String ESTOQUE_ENTITY_QUANTIDADE_A_RESERVAR_DEVE_SER_MAIOR_QUE_ZERO = "Quantidade a reservar deve ser maior que zero";
    public static final String ESTOQUE_ENTITY_QUANTIDADE_INDISPONIVEL_EM_ESTOQUE = "Quantidade indispon\u00EDvel em estoque";

    public static final String AUTH_SERV_DADOS_DE_LOGIN_INVALIDOS = "Dados de login inv\u00E1lidos";
    public static final String AUTH_SERV_E_MAIL_JA_CADASTRADO = "E-mail j\u00E1 cadastrado";

    public static final String DIAGNOSTICO_INVALIDO = "Diagn\u00F3stico inv\u00E1lido";
    public static final String INFORME_AO_MENOS_UM_SERVICO_PARA_O_DIAGNOSTICO = "Informe ao menos um servi\u00E7o para o diagn\u00F3stico";
}
