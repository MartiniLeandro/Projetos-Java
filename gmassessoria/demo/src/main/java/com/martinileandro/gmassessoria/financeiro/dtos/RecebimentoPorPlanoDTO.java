package com.martinileandro.gmassessoria.financeiro.dtos;

import com.martinileandro.gmassessoria.plano.dtos.RecebimentoPorPlanoProjection;

import java.math.BigDecimal;

public record RecebimentoPorPlanoDTO(String plano, BigDecimal valorRecebido) {
    public RecebimentoPorPlanoDTO(RecebimentoPorPlanoProjection data){
        this(
                data.getPlano(), data.getValorRecebido()
        );
    }
}
