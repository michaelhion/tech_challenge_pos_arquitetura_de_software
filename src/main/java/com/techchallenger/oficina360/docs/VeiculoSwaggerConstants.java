package com.techchallenger.oficina360.docs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VeiculoSwaggerConstants {

    /*
     * Tag
     */
    public static final String TAG_NAME = "4 - Veículos";

    public static final String TAG_DESCRIPTION =
            "Endpoints administrativos para cadastro, consulta, edição e exclusão de veículos da oficina. "
                    + "Os veículos são utilizados na abertura e acompanhamento das ordens de serviço.";

    /*
     * Buscar por placa
     */
    public static final String SUMMARY_BUSCAR_POR_PLACA =
            "Buscar veículo por placa";

    public static final String DESCRIPTION_BUSCAR_POR_PLACA =
            "Retorna os dados de um veículo específico a partir da placa informada.";

    public static final String RESPONSE_VEICULO_ENCONTRADO =
            "Veículo encontrado com sucesso.";

    public static final String PARAM_PLACA_DESCRIPTION =
            "Placa do veículo no padrão antigo ABC1234 ou Mercosul ABC1D23, sem hífen.";

    /*
     * Salvar
     */
    public static final String SUMMARY_SALVAR =
            "Cadastrar novo veículo";

    public static final String DESCRIPTION_SALVAR =
            "Cadastra um novo veículo da oficina. "
                    + "O veículo deve possuir placa, marca, modelo e ano, "
                    + "conforme exigido no fluxo de criação da ordem de serviço.";

    public static final String RESPONSE_VEICULO_CADASTRADO =
            "Veículo cadastrado com sucesso.";

    public static final String REQUEST_BODY_SALVAR =
            "Dados do veículo que será cadastrado.";

    public static final String DESCRIPTION_BAD_REQUEST_SALVAR =
            "Dados inválidos. Verifique placa, marca, modelo e ano.";

    public static final String DESCRIPTION_VEICULO_CONFLICT =
            "Já existe veículo cadastrado com a mesma placa.";

    /*
     * Editar por placa
     */
    public static final String SUMMARY_EDITAR_POR_PLACA =
            "Atualizar veículo por placa";

    public static final String DESCRIPTION_EDITAR_POR_PLACA =
            "Atualiza os dados de um veículo já cadastrado a partir da placa atual. "
                    + "Permite alterar placa, marca, modelo e ano, respeitando as validações do domínio.";

    public static final String RESPONSE_VEICULO_ATUALIZADO =
            "Veículo atualizado com sucesso.";

    public static final String PARAM_PLACA_ATUAL_DESCRIPTION =
            "Placa atual do veículo que será atualizado.";

    public static final String REQUEST_BODY_EDITAR =
            "Novos dados do veículo, incluindo placa, marca, modelo e ano.";

    public static final String DESCRIPTION_BAD_REQUEST_EDITAR_POR_PLACA =
            "Dados inválidos. Verifique se a placa informada na URL e a placa enviada no corpo estão no padrão antigo ABC1234 ou Mercosul ABC1D23, sem hífen.";

    public static final String DESCRIPTION_VEICULO_CONFLICT_EDITAR =
            "Conflito de dados. A nova placa informada já está cadastrada para outro veículo.";

    /*
     * Deletar por placa
     */
    public static final String SUMMARY_DELETAR_POR_PLACA =
            "Excluir veículo por placa";

    public static final String DESCRIPTION_DELETAR_POR_PLACA =
            "Remove um veículo cadastrado no sistema a partir da placa informada. "
                    + "A exclusão pode ser impedida caso o veículo esteja vinculado a uma ordem de serviço.";

    public static final String RESPONSE_VEICULO_EXCLUIDO =
            "Veículo excluído com sucesso.";

    public static final String PARAM_PLACA_DELETE_DESCRIPTION =
            "Placa do veículo que será excluído.";

    public static final String DESCRIPTION_VEICULO_CONFLICT_VINCULO =
            "Veículo possui vínculo com ordem de serviço e não pode ser excluído.";

    /*
     * Listar
     */
    public static final String SUMMARY_LISTAR =
            "Listar veículos";

    public static final String DESCRIPTION_LISTAR =
            "Retorna a lista de todos os veículos cadastrados na oficina.";

    public static final String RESPONSE_LISTA_VEICULOS =
            "Lista de veículos retornada com sucesso.";

    /*
     * Erros específicos
     */
    public static final String DESCRIPTION_VEICULO_NOT_FOUND =
            "Veículo não encontrado.";
}