package com.martinileandro.gmassessoria.fatura.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ListagemFaturasDTO(Long id, LocalDate dataVencimento, LocalDate dataPagamento, Integer numeroParcelas, String aluno, String plano, String ciclo, BigDecimal valorCobrado, String status, String formaPagamento) {
    public ListagemFaturasDTO(ListagemFaturasProjection data){
        this(
                data.getId(),
                data.getDataVencimento(),
                data.getDataPagamento(),
                data.getNumeroParcela(),
                data.getAluno(),
                data.getPlano(),
                data.getCiclo(),
                data.getValorCobrado(),
                data.getStatus(),
                data.getFormaPagamento()
        );
    }
}
