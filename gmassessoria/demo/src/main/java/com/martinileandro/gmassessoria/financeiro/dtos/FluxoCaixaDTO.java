package com.martinileandro.gmassessoria.financeiro.dtos;

import java.math.BigDecimal;

public record FluxoCaixaDTO(Integer mes, Integer ano, BigDecimal valorPrevisto, BigDecimal valorRecebido) {
    public FluxoCaixaDTO(FluxoCaixaProjection data){
        this(
                data.getMes(), data.getAno(), data.getPrevisto(), data.getRecebido()
        );
    }
}
