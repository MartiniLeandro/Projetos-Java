package com.martinileandro.gmassessoria.financeiro.dtos;

import java.math.BigDecimal;

public interface FluxoCaixaProjection {
    Integer getMes();
    Integer getAno();
    BigDecimal getPrevisto();
    BigDecimal getRecebido();
}
