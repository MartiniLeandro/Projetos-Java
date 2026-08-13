package com.martinileandro.gmassessoria.plano.dtos;

import com.martinileandro.gmassessoria.plano.Ciclo;

import java.math.BigDecimal;

public record PlanoRequestDTO(String nome, Ciclo ciclo, BigDecimal valorBase) {
}
