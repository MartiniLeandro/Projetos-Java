package com.martinileandro.gmassessoria.plano.dtos;

import com.martinileandro.gmassessoria.plano.Ciclo;
import com.martinileandro.gmassessoria.plano.Plano;
import com.martinileandro.gmassessoria.plano.PlanoStatus;

import java.math.BigDecimal;

public record PlanoResponseDTO(Long id, String nome, Ciclo ciclo, BigDecimal valorBase, PlanoStatus status) {
    public PlanoResponseDTO(Plano plano){
        this(
                plano.getId(), plano.getNome(), plano.getCiclo(), plano.getValorBase(), plano.getPlanoStatus()
        );
    }

}
