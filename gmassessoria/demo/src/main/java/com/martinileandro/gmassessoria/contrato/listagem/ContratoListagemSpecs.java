package com.martinileandro.gmassessoria.contrato.listagem;

import com.martinileandro.gmassessoria.plano.PlanoCategoria;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ContratoListagemSpecs {
    public static Specification<ContratoListagemView> nomeAlunoContem(String nome) {
        return (root, query, cb) ->
                nome == null || nome.isBlank() ? null : cb.like(cb.lower(root.get("nomeAluno")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<ContratoListagemView> planoCategoriaIgual(PlanoCategoria categoria) {
        return (root, query, cb) ->
                categoria == null ? null : cb.equal(root.get("planoCategoria"), categoria);
    }

    public static Specification<ContratoListagemView> cicloPlanoIgual(String ciclo) {
        return (root, query, cb) ->
                ciclo == null || ciclo.isBlank() ? null : cb.equal(root.get("cicloPlano"), ciclo);
    }

    public static Specification<ContratoListagemView> statusContratoIgual(String status) {
        return (root, query, cb) ->
                status == null || status.isBlank() ? null : cb.equal(root.get("statusContrato"), status);
    }

    public static Specification<ContratoListagemView> situacaoFinanceiraIgual(String situacao) {
        return (root, query, cb) ->
                situacao == null || situacao.isBlank() ? null : cb.equal(root.get("situacaoFinanceira"), situacao);
    }

    public static Specification<ContratoListagemView> tempoRestanteEntre(Integer minDias, Integer maxDias) {
        return (root, query, cb) -> {
            if (minDias != null && maxDias != null) return cb.between(root.get("tempoRestante"), minDias, maxDias);
            if (minDias != null) return cb.greaterThanOrEqualTo(root.get("tempoRestante"), minDias);
            if (maxDias != null) return cb.lessThanOrEqualTo(root.get("tempoRestante"), maxDias);
            return null;
        };
    }

    public static Specification<ContratoListagemView> dataInicioEntre(LocalDate inicio, LocalDate fim) {
        return (root, query, cb) -> {
            if (inicio != null && fim != null) return cb.between(root.get("dataInicio"), inicio, fim);
            if (inicio != null) return cb.greaterThanOrEqualTo(root.get("dataInicio"), inicio);
            if (fim != null) return cb.lessThanOrEqualTo(root.get("dataInicio"), fim);
            return null;
        };
    }
}
