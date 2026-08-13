package com.martinileandro.gmassessoria.aluno.listagem;

import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class AlunoListagemSpecs {

    public static Specification<AlunoListagemView> nomeContem(String nome) {
        return (root, query, builder) -> {
            if (nome == null || nome.isBlank()) return null;
            return builder.like(builder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    public static Specification<AlunoListagemView> planoIgual(String plano) {
        return (root, query, builder) -> {
            if (plano == null || plano.isBlank()) return null;
            return builder.equal(root.get("plano"), plano);
        };
    }

    public static Specification<AlunoListagemView> statusAlunoIgual(String statusAluno) {
        return (root, query, builder) -> {
            if (statusAluno == null || statusAluno.isBlank()) return null;
            return builder.equal(root.get("statusAluno"), statusAluno);
        };
    }

    public static Specification<AlunoListagemView> statusFinanceiroIgual(String statusFinanceiro) {
        return (root, query, builder) -> {
            if (statusFinanceiro == null || statusFinanceiro.isBlank()) return null;
            return builder.equal(root.get("statusFinanceiro"), statusFinanceiro);
        };
    }

    public static Specification<AlunoListagemView> tempoRestanteEntre(Integer minDias, Integer maxDias) {
        return (root, query, builder) -> {
            if (minDias == null && maxDias == null) return null;

            if (minDias != null && maxDias != null) {
                return builder.between(root.get("tempoRestante"), minDias, maxDias);
            } else if (minDias != null) {
                return builder.greaterThanOrEqualTo(root.get("tempoRestante"), minDias);
            } else {
                return builder.lessThanOrEqualTo(root.get("tempoRestante"), maxDias);
            }
        };
    }

    public static Specification<AlunoListagemView> dataInicioEntre(LocalDate dataMin, LocalDate dataMax) {
        return (root, query, builder) -> {
            if (dataMin == null && dataMax == null) return null;

            if (dataMin != null && dataMax != null) {
                return builder.between(root.get("dataInicio"), dataMin, dataMax);
            } else if (dataMin != null) {
                return builder.greaterThanOrEqualTo(root.get("dataInicio"), dataMin);
            } else {
                return builder.lessThanOrEqualTo(root.get("dataInicio"), dataMax);
            }
        };
    }

    public static Specification<AlunoListagemView> dataFimEntre(LocalDate dataMin, LocalDate dataMax) {
        return (root, query, builder) -> {
            if (dataMin == null && dataMax == null) return null;

            if (dataMin != null && dataMax != null) {
                return builder.between(root.get("dataFim"), dataMin, dataMax);
            } else if (dataMin != null) {
                return builder.greaterThanOrEqualTo(root.get("dataFim"), dataMin);
            } else {
                return builder.lessThanOrEqualTo(root.get("dataFim"), dataMax);
            }
        };
    }
}