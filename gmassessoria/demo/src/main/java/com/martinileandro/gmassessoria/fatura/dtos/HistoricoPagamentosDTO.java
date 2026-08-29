package com.martinileandro.gmassessoria.fatura.dtos;

import com.martinileandro.gmassessoria.contrato.FormaPagamento;
import com.martinileandro.gmassessoria.fatura.FaturaStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HistoricoPagamentosDTO(LocalDate dataVencimento, LocalDate dataPagamento, BigDecimal valorPagamento, FaturaStatus statusPagamento, FormaPagamento formaPagamento) {
    public HistoricoPagamentosDTO(HistoricoPagamentosProjection data){
        this(
                data.getDataVencimento(), data.getDataPagamento(), data.getValorCobrado(), data.getStatus(), data.getFormaPagamento()
        );
    }
}
