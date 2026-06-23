package com.techchallenger.oficina360.services.validators;

import com.techchallenger.oficina360.entities.Estoque;
import com.techchallenger.oficina360.entities.Servico;
import com.techchallenger.oficina360.exceptions.RegraDeNegocioException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DiagnosticoValidator {

    public void validar(
            List<String> codigosServicos,
            Map<String, Integer> quantidadeEstoquePorCodigo,
            Map<String, Servico> servicosPorCodigo,
            Map<String, Estoque> estoquesPorCodigo
    ) {
        List<String> erros = new ArrayList<>();

        validarServicosEncontrados(codigosServicos, servicosPorCodigo, erros);
        validarItensEstoqueEncontrados(quantidadeEstoquePorCodigo, estoquesPorCodigo, erros);
        validarDisponibilidadeEstoque(quantidadeEstoquePorCodigo, estoquesPorCodigo, erros);

        if (!erros.isEmpty()) {
            throw new RegraDeNegocioException("Diagnóstico inválido", erros);
        }
    }

    private void validarServicosEncontrados(
            List<String> codigosServicos,
            Map<String, Servico> servicosPorCodigo,
            List<String> erros
    ) {
        if (codigosServicos == null || codigosServicos.isEmpty()) {
            erros.add("Informe ao menos um serviço para o diagnóstico");
            return;
        }

        List<String> codigosNaoEncontrados = codigosServicos.stream()
                .filter(codigo -> !servicosPorCodigo.containsKey(codigo))
                .toList();

        if (!codigosNaoEncontrados.isEmpty()) {
            erros.add("Serviços não encontrados: " + String.join(", ", codigosNaoEncontrados));
        }
    }

    private void validarItensEstoqueEncontrados(
            Map<String, Integer> quantidadeEstoquePorCodigo,
            Map<String, Estoque> estoquesPorCodigo,
            List<String> erros
    ) {
        if (quantidadeEstoquePorCodigo == null || quantidadeEstoquePorCodigo.isEmpty()) {
            return;
        }

        Set<String> codigosEncontrados = estoquesPorCodigo.keySet();

        List<String> codigosNaoEncontrados = quantidadeEstoquePorCodigo.keySet()
                .stream()
                .filter(codigo -> !codigosEncontrados.contains(codigo))
                .toList();

        if (!codigosNaoEncontrados.isEmpty()) {
            erros.add("Itens de estoque não encontrados: " + String.join(", ", codigosNaoEncontrados));
        }
    }

    private void validarDisponibilidadeEstoque(
            Map<String, Integer> quantidadeEstoquePorCodigo,
            Map<String, Estoque> estoquesPorCodigo,
            List<String> erros) {
        if (quantidadeEstoquePorCodigo == null || quantidadeEstoquePorCodigo.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Integer> entry : quantidadeEstoquePorCodigo.entrySet()) {

            String codigo = entry.getKey();
            Integer quantidadeSolicitada = entry.getValue();

            Estoque estoque = estoquesPorCodigo.get(codigo);

            if (estoque != null) {

                if (quantidadeSolicitada == null || quantidadeSolicitada <= 0) {

                    erros.add("Quantidade inválida para o item de estoque "+ codigo);

                } else if (quantidadeSolicitada> estoque.getDisponiveis()) {
                    erros.add(String.format("Estoque insuficiente para %s. Solicitado: %d, disponível: %d",
                                    codigo,quantidadeSolicitada,estoque.getDisponiveis()));
                }
            }
        }
    }
}