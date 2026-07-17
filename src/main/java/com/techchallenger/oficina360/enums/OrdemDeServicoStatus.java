package com.techchallenger.oficina360.enums;

import java.util.Arrays;
import java.util.List;

public enum OrdemDeServicoStatus {
    RECEBIDA(true),
    EM_DIAGNOSTICO(true),
    AGUARDANDO_APROVACAO(true),
    ORCAMENTO_APROVADO(true),
    ORCAMENTO_REPROVADO(false),
    EM_EXECUCAO(true),
    FINALIZADA(true),
    ENTREGUE(false);

    private final boolean ativa;

    OrdemDeServicoStatus(boolean ativa) {
        this.ativa = ativa;
    }

    public boolean isAtiva() {
        return ativa;
    }

    private static final List<OrdemDeServicoStatus> ATIVOS =
            Arrays.stream(values())
                    .filter(OrdemDeServicoStatus::isAtiva)
                    .toList();

    public static List<OrdemDeServicoStatus> ativos() {
        return ATIVOS;
    }
}