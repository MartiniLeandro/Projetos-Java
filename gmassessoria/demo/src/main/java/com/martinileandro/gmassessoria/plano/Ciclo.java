package com.martinileandro.gmassessoria.plano;

import lombok.Getter;

@Getter
public enum Ciclo {
    MENSAL(1),
    BIMESTRAL(2),
    TRIMESTRAL(3),
    SEMESTRAL(6),
    ANUAL(12);

    private final int quantidadeMeses;

    Ciclo(int quantidadeMeses) {
        this.quantidadeMeses = quantidadeMeses;
    }
}
