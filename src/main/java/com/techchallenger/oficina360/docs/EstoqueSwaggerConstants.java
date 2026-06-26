package com.techchallenger.oficina360.docs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EstoqueSwaggerConstants {

    /*
     * Tag
     */
    public static final String TAG_NAME = "5 - Estoque";

    public static final String TAG_DESCRIPTION =
            "Endpoints administrativos para cadastro, consulta, edição e exclusão de peças e insumos do estoque da oficina.";

    /*
     * Buscar por ID
     */
    public static final String SUMMARY_BUSCAR_POR_CODIGO =
            "Buscar item de estoque por código";

    public static final String DESCRIPTION_BUSCAR_POR_CODIGO =
            "Retorna os dados de um item de estoque específico a partir do seu código.";

    public static final String RESPONSE_ESTOQUE_ENCONTRADO =
            "Item de estoque encontrado com sucesso.";

    public static final String PARAM_CODIGO_DESCRIPTION =
            "Código único do item de estoque.";
    public static final String PARAM_ID_DESCRIPTION =
            "Identificador único do item de estoque no formato UUID.";

    /*
     * Salvar
     */
    public static final String SUMMARY_SALVAR =
            "Cadastrar novo item de estoque";

    public static final String DESCRIPTION_SALVAR =
            "Cadastra uma nova peça ou insumo no estoque da oficina, informando nome, valor, quantidade e reservas.";

    public static final String RESPONSE_ESTOQUE_CADASTRADO =
            "Item de estoque cadastrado com sucesso.";

    public static final String REQUEST_BODY_SALVAR =
            "Dados do item de estoque que será cadastrado.";

    public static final String DESCRIPTION_BAD_REQUEST_SALVAR =
            "Dados inválidos. Verifique se nome, valor, quantidade e reservados foram informados corretamente.";

    public static final String DESCRIPTION_ESTOQUE_CONFLICT =
            "Já existe item de estoque cadastrado com o mesmo nome.";

    /*
     * Editar
     */
    public static final String SUMMARY_EDITAR =
            "Atualizar item de estoque existente";

    public static final String DESCRIPTION_EDITAR =
            "Atualiza os dados de uma peça ou insumo já cadastrado no estoque a partir do seu ID.";

    public static final String RESPONSE_ESTOQUE_ATUALIZADO =
            "Item de estoque atualizado com sucesso.";

    public static final String REQUEST_BODY_EDITAR =
            "Novos dados do item de estoque.";

    public static final String DESCRIPTION_BAD_REQUEST_EDITAR =
            "Dados inválidos. Verifique se o ID está no formato UUID e se os campos foram informados corretamente.";

    public static final String DESCRIPTION_ESTOQUE_CONFLICT_EDITAR =
            "Conflito de dados. Já existe outro item de estoque cadastrado com o mesmo nome.";

    /*
     * Deletar
     */
    public static final String SUMMARY_DELETAR =
            "Excluir item de estoque";

    public static final String DESCRIPTION_DELETAR =
            "Remove uma peça ou insumo cadastrado no estoque a partir do seu ID. "
                    + "A exclusão pode ser impedida caso o item esteja vinculado a uma ordem de serviço.";

    public static final String RESPONSE_ESTOQUE_EXCLUIDO =
            "Item de estoque excluído com sucesso.";

    public static final String PARAM_CODIGO_DELETE_DESCRIPTION =
            "Codigo único do item de estoque que será excluído.";

    public static final String DESCRIPTION_ESTOQUE_CONFLICT_VINCULO =
            "Item de estoque possui vínculo com ordem de serviço e não pode ser excluído.";

    /*
     * Listar
     */
    public static final String SUMMARY_LISTAR =
            "Listar itens de estoque";

    public static final String DESCRIPTION_LISTAR =
            "Retorna a lista de todas as peças e insumos cadastrados no estoque da oficina.";

    public static final String RESPONSE_LISTA_ESTOQUE =
            "Lista de itens de estoque retornada com sucesso.";

    /*
     * Erros específicos
     */
    public static final String DESCRIPTION_ESTOQUE_NOT_FOUND =
            "Item de estoque não encontrado.";

    /*
     * Reservar estoque
     */
    public static final String SUMMARY_RESERVAR =
            "Reservar quantidade de item de estoque";

    public static final String DESCRIPTION_RESERVAR =
            "Reserva uma quantidade disponível de uma peça ou insumo do estoque. "
                    + "A quantidade reservada será abatida dos itens disponíveis, mas ainda não será removida do estoque total.";

    public static final String RESPONSE_ESTOQUE_RESERVADO =
            "Quantidade reservada com sucesso.";

    public static final String REQUEST_BODY_RESERVAR =
            "Quantidade que será reservada no item de estoque.";

    public static final String DESCRIPTION_BAD_REQUEST_RESERVAR =
            "Dados inválidos. Verifique se o ID está no formato UUID e se a quantidade informada é maior que zero.";

    public static final String DESCRIPTION_ESTOQUE_INDISPONIVEL =
            "Quantidade indisponível para reserva no estoque.";
}