package com.techchallenger.oficina360.docs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServicoSwaggerConstants {

    /*
     * Tag
     */
    public static final String TAG_NAME = "4 - Serviços";

    public static final String TAG_DESCRIPTION =
            "Endpoints administrativos para cadastro, consulta, edição e exclusão dos serviços oferecidos pela oficina.";

    /*
     * Buscar por ID
     */
    public static final String SUMMARY_BUSCAR_POR_CODIGO =
            "Buscar serviço por código";
    public static final String SUMMARY_BUSCAR_POR_ID =
            "Buscar serviço por ID";

    public static final String DESCRIPTION_BUSCAR_POR_CODIGO =
            "Retorna os dados de um serviço específico a partir do seu código único.";
    public static final String DESCRIPTION_BUSCAR_POR_ID =
            "Retorna os dados de um serviço específico a partir do seu identificador único UUID.";

    public static final String RESPONSE_SERVICO_ENCONTRADO =
            "Serviço encontrado com sucesso.";

    public static final String PARAM_CODIGO_DESCRIPTION =
            "Código único do serviço.";
    public static final String PARAM_ID_DESCRIPTION =
            "Identificador único do serviço no formato UUID.";

    /*
     * Salvar
     */
    public static final String SUMMARY_SALVAR =
            "Cadastrar novo serviço";

    public static final String DESCRIPTION_SALVAR =
            "Cadastra um novo serviço oferecido pela oficina, informando descrição e valor.";

    public static final String RESPONSE_SERVICO_CADASTRADO =
            "Serviço cadastrado com sucesso.";

    public static final String REQUEST_BODY_SALVAR =
            "Dados do serviço que será cadastrado.";

    public static final String DESCRIPTION_BAD_REQUEST_SALVAR =
            "Dados inválidos. Verifique se descrição e valor foram informados corretamente.";

    public static final String DESCRIPTION_SERVICO_CONFLICT =
            "Já existe serviço cadastrado com a mesma descrição.";

    /*
     * Editar
     */
    public static final String SUMMARY_EDITAR =
            "Atualizar serviço existente";

    public static final String DESCRIPTION_EDITAR =
            "Atualiza os dados de um serviço já cadastrado a partir do seu ID.";

    public static final String RESPONSE_SERVICO_ATUALIZADO =
            "Serviço atualizado com sucesso.";

    public static final String REQUEST_BODY_EDITAR =
            "Novos dados do serviço.";

    public static final String DESCRIPTION_BAD_REQUEST_EDITAR =
            "Dados inválidos ou ID em formato incorreto.";

    public static final String DESCRIPTION_SERVICO_CONFLICT_EDITAR =
            "Conflito de dados, como descrição já utilizada por outro serviço.";

    /*
     * Deletar
     */
    public static final String SUMMARY_DELETAR =
            "Excluir serviço";

    public static final String DESCRIPTION_DELETAR =
            "Remove um serviço cadastrado no sistema a partir do seu ID. "
                    + "A exclusão pode ser impedida caso o serviço esteja vinculado a uma ordem de serviço.";

    public static final String RESPONSE_SERVICO_EXCLUIDO =
            "Serviço excluído com sucesso.";

    public static final String PARAM_CODIGO_DELETE_DESCRIPTION =
            "Código único do serviço que será excluído.";

    public static final String DESCRIPTION_SERVICO_CONFLICT_VINCULO =
            "Serviço possui vínculo com ordem de serviço e não pode ser excluído.";

    /*
     * Listar
     */
    public static final String SUMMARY_LISTAR =
            "Listar serviços";

    public static final String DESCRIPTION_LISTAR =
            "Retorna a lista de todos os serviços cadastrados na oficina.";

    public static final String RESPONSE_LISTA_SERVICOS =
            "Lista de serviços retornada com sucesso.";

    /*
     * Erros específicos
     */
    public static final String DESCRIPTION_SERVICO_NOT_FOUND =
            "Serviço não encontrado.";
}