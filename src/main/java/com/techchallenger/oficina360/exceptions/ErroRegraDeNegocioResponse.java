package com.techchallenger.oficina360.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record ErroRegraDeNegocioResponse(
        int status,
        String erro,
        String mensagem,
        List<String> detalhes,
        LocalDateTime timestamp
) {
}