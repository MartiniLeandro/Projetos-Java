package com.martinileandro.gmassessoria.aluno.dtos;

import java.time.LocalDate;

public record AlunoListagemFilterDTO(String nome, String plano, String statusAluno, String statusFinanceiro, Integer minDias, Integer maxDias, LocalDate inicioMin, LocalDate inicioMax, LocalDate fimMin, LocalDate fimMax) {
}
