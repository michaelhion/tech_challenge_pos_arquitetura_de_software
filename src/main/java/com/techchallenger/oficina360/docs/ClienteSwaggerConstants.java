package com.techchallenger.oficina360.docs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClienteSwaggerConstants {

    /*
     * Tag
     */
    public static final String TAG_NAME = "2 - Clientes";

    public static final String TAG_DESCRIPTION =
            "Administra clientes, realizando operações de cadastro, consulta, edição e exclusão.";

    /*
     * Buscar por documento
     */
    public static final String SUMMARY_BUSCAR_POR_DOCUMENTO = "Buscar cliente por documento";

    public static final String DESCRIPTION_BUSCAR_POR_DOCUMENTO =
            "Retorna os dados cadastrais de um cliente específico a partir do seu documento, CPF ou CNPJ.";

    public static final String RESPONSE_CLIENTE_ENCONTRADO =
            "Cliente encontrado com sucesso.";

    public static final String PARAM_DOCUMENTO_DESCRIPTION =
            "Documento do cliente, CPF ou CNPJ, somente números.";

    /*
     * Salvar
     */
    public static final String SUMMARY_SALVAR = "Cadastrar novo cliente";

    public static final String DESCRIPTION_SALVAR =
            "Cadastra um novo cliente da oficina. "
                    + "O cliente deve possuir dados válidos, incluindo CPF ou CNPJ, "
                    + "conforme as regras de validação do sistema.";

    public static final String RESPONSE_CLIENTE_CADASTRADO =
            "Cliente cadastrado com sucesso.";

    public static final String REQUEST_BODY_SALVAR =
            "Dados do cliente que será cadastrado.";

    public static final String DESCRIPTION_BAD_REQUEST_SALVAR =
            "Dados inválidos. Verifique campos obrigatórios, CPF/CNPJ e demais validações.";

    /*
     * Editar
     */
    public static final String SUMMARY_EDITAR = "Atualizar cliente existente";

    public static final String DESCRIPTION_EDITAR =
            "Atualiza os dados cadastrais de um cliente já existente a partir do seu ID. "
                    + "Utilize este endpoint para alterar informações como nome, telefone, e-mail ou CPF/CNPJ, "
                    + "respeitando as validações do domínio.";

    public static final String RESPONSE_CLIENTE_ATUALIZADO =
            "Cliente atualizado com sucesso.";

    public static final String PARAM_ID_DESCRIPTION =
            "Identificador único do cliente no formato UUID.";

    public static final String REQUEST_BODY_EDITAR =
            "Novos dados do cliente.";

    public static final String DESCRIPTION_BAD_REQUEST_EDITAR =
            "Dados inválidos ou ID em formato incorreto.";

    public static final String DESCRIPTION_CLIENTE_CONFLICT_EDITAR =
            "Conflito de dados, como CPF/CNPJ já utilizado por outro cliente.";

    /*
     * Deletar
     */
    public static final String SUMMARY_DELETAR = "Excluir cliente";

    public static final String DESCRIPTION_DELETAR =
            "Remove um cliente cadastrado no sistema a partir do seu documento. "
                    + "A exclusão pode ser impedida caso o cliente esteja vinculado a veículos ou ordens de serviço.";

    public static final String RESPONSE_CLIENTE_EXCLUIDO =
            "Cliente excluído com sucesso.";

    public static final String PARAM_DOCUMENTO_DELETE_DESCRIPTION =
            "Documento do cliente que será excluído, CPF ou CNPJ, somente números.";

    /*
     * Listar
     */
    public static final String SUMMARY_LISTAR = "Listar clientes";

    public static final String DESCRIPTION_LISTAR =
            "Retorna a lista de todos os clientes cadastrados na oficina. "
                    + "Este endpoint apoia a gestão administrativa do sistema.";

    public static final String RESPONSE_LISTA_CLIENTES =
            "Lista de clientes retornada com sucesso.";

    /*
     * Erros específicos
     */
    public static final String DESCRIPTION_CLIENTE_NOT_FOUND =
            "Cliente não encontrado.";

    public static final String DESCRIPTION_CLIENTE_CONFLICT =
            "Já existe cliente cadastrado com o mesmo CPF/CNPJ.";

    public static final String DESCRIPTION_CLIENTE_CONFLICT_VINCULOS =
            "Cliente possui vínculos com veículos ou ordens de serviço e não pode ser excluído.";
}