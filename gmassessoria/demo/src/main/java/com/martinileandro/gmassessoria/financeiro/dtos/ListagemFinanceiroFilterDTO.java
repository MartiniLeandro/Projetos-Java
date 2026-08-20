package com.martinileandro.gmassessoria.financeiro.dtos;

import com.martinileandro.gmassessoria.fatura.FaturaStatus;

public record ListagemFinanceiroFilterDTO(Integer mes, Integer ano, String nomeAluno, FaturaStatus status) {
}
