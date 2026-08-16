package com.martinileandro.gmassessoria.fatura.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ListagemFaturasProjection{
    LocalDate getDataVencimento();
    Integer getNumeroParcela();
    String getAluno();
    String getPlano();
    String getCiclo();
    BigDecimal getValorCobrado();
    String getStatus();
}
