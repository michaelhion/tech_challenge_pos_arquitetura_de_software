package com.techchallenger.oficina360.exceptions;

import java.util.List;

public class RegraDeNegocioException extends RuntimeException {

    private final List<String> mensagens;

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
        this.mensagens = List.of(mensagem);
    }

    public RegraDeNegocioException(String mensagem, List<String> mensagens) {
        super(mensagem);
        this.mensagens = mensagens == null ? List.of() : List.copyOf(mensagens);
    }

    public List<String> getMensagens() {
        return mensagens;
    }
}