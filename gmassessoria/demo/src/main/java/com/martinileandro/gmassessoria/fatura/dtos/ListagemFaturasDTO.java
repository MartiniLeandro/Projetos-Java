package com.martinileandro.gmassessoria.fatura.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ListagemFaturasDTO(LocalDate dataVencimento, Integer numeroParcelas, String aluno, String plano, String ciclo, BigDecimal valorCobrado, String status) {
    public ListagemFaturasDTO(ListagemFaturasProjection data){
        this(
                data.getDataVencimento(),
                data.getNumeroParcela(),
                data.getAluno(),
                data.getPlano(),
                data.getCiclo(),
                data.getValorCobrado(),
                data.getStatus()
        );
    }
}
