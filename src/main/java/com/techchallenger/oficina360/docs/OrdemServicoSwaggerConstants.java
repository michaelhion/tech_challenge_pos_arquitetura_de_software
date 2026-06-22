package com.techchallenger.oficina360.docs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrdemServicoSwaggerConstants {

    public static final String TAG_NAME = "6 - Ordens de Serviço";

    public static final String TAG_DESCRIPTION =
            "Endpoints relacionados à criação, consulta, edição, exclusão, aprovação e acompanhamento de ordens de serviço.";

    public static final String SUMMARY_BUSCAR_POR_ID =
            "Buscar ordem de serviço por ID";

    public static final String DESCRIPTION_BUSCAR_POR_ID =
            "Retorna os dados de uma ordem de serviço específica a partir do seu identificador único UUID.";

    public static final String RESPONSE_ORDEM_SERVICO_ENCONTRADA =
            "Ordem de serviço encontrada com sucesso.";

    public static final String DESCRIPTION_ORDEM_SERVICO_NOT_FOUND =
            "Ordem de serviço não encontrada.";

    public static final String PARAM_ID_DESCRIPTION =
            "Identificador único da ordem de serviço no formato UUID.";

    public static final String SUMMARY_SALVAR =
            "Criar nova ordem de serviço";

    public static final String DESCRIPTION_SALVAR =
            "Cria uma nova ordem de serviço vinculada a um cliente e veículo, permitindo diagnóstico, orçamento, aprovação e execução.";

    public static final String RESPONSE_ORDEM_SERVICO_CADASTRADA =
            "Ordem de serviço criada com sucesso.";

    public static final String DESCRIPTION_BAD_REQUEST_SALVAR =
            "Dados inválidos. Verifique os campos obrigatórios da ordem de serviço.";

    public static final String SUMMARY_EDITAR =
            "Atualizar ordem de serviço existente";

    public static final String DESCRIPTION_EDITAR =
            "Atualiza os dados de uma ordem de serviço já cadastrada a partir do seu ID.";

    public static final String RESPONSE_ORDEM_SERVICO_ATUALIZADA =
            "Ordem de serviço atualizada com sucesso.";

    public static final String DESCRIPTION_BAD_REQUEST_EDITAR =
            "Dados inválidos. Verifique se o ID está no formato UUID e se os campos foram informados corretamente.";

    public static final String SUMMARY_DELETAR =
            "Excluir ordem de serviço";

    public static final String DESCRIPTION_DELETAR =
            "Remove uma ordem de serviço cadastrada no sistema a partir do seu ID.";

    public static final String RESPONSE_ORDEM_SERVICO_EXCLUIDA =
            "Ordem de serviço excluída com sucesso.";

    public static final String DESCRIPTION_ORDEM_SERVICO_CONFLICT_VINCULO =
            "Ordem de serviço não pode ser excluída devido ao seu estado atual ou vínculos existentes.";

    public static final String SUMMARY_APROVAR =
            "Registrar aprovação ou reprovação do orçamento";

    public static final String DESCRIPTION_APROVAR =
            "Registra a aprovação ou reprovação do orçamento da ordem de serviço. "
                    + "A ação só deve ser permitida quando a OS estiver aguardando aprovação.";

    public static final String RESPONSE_ORDEM_SERVICO_APROVADA =
            "Aprovação ou reprovação registrada com sucesso.";

    public static final String REQUEST_BODY_APROVAR =
            "Dados de aprovação ou reprovação do orçamento.";

    public static final String DESCRIPTION_BAD_REQUEST_APROVAR =
            "Dados inválidos. Informe se o orçamento foi aprovado ou reprovado.";

    public static final String DESCRIPTION_ORDEM_SERVICO_CONFLICT_APROVAR =
            "A ordem de serviço não está em um status que permita aprovação ou reprovação.";
    public static final String SUMMARY_LISTAR =
            "Listar ordens de serviço";

    public static final String DESCRIPTION_LISTAR =
            "Retorna a lista de todas as ordens de serviço cadastradas no sistema.";

    public static final String RESPONSE_LISTA_ORDENS_SERVICO =
            "Lista de ordens de serviço retornada com sucesso.";
    public static final String SUMMARY_DIAGNOSTICAR =
            "Registrar diagnóstico da ordem de serviço";

    public static final String DESCRIPTION_DIAGNOSTICAR =
            "Registra o diagnóstico da ordem de serviço, definindo o serviço que será executado e copiando o valor atual do serviço para a OS.";

    public static final String RESPONSE_ORDEM_SERVICO_DIAGNOSTICADA =
            "Diagnóstico registrado com sucesso.";

    public static final String REQUEST_BODY_DIAGNOSTICAR =
            "Dados do diagnóstico da ordem de serviço, contendo o serviço definido.";

    public static final String DESCRIPTION_BAD_REQUEST_DIAGNOSTICAR =
            "Dados inválidos. Verifique se o ID da ordem de serviço e o ID do serviço foram informados corretamente.";

    public static final String DESCRIPTION_ORDEM_SERVICO_CONFLICT_DIAGNOSTICAR =
            "A ordem de serviço não está em um status que permita diagnóstico.";

    public static final String REQUEST_BODY_SALVAR =
            "Dados da Ordem de serviço que será cadastrado.";
    public static final String REQUEST_BODY_EDITAR =
            "Novos dados da ordem de serviço para atualização.";

    public static final String PARAM_ID_DELETE_DESCRIPTION =
            "Identificador único do item de Ordem de serviço que será excluído.";
}