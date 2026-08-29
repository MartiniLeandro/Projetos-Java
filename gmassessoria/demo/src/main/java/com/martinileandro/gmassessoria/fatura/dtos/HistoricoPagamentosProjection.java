package com.martinileandro.gmassessoria.fatura.dtos;

import com.martinileandro.gmassessoria.contrato.FormaPagamento;
import com.martinileandro.gmassessoria.fatura.FaturaStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface HistoricoPagamentosProjection {
    LocalDate getDataVencimento();
    LocalDate getDataPagamento();
    BigDecimal getValorCobrado();
    FaturaStatus getStatus();
    FormaPagamento getFormaPagamento();

}
