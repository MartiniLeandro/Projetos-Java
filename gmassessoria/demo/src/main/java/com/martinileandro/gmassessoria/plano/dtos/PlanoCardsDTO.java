package com.martinileandro.gmassessoria.plano.dtos;

import java.math.BigDecimal;

public record PlanoCardsDTO(Long totalPlanos, Long planosAtivos, BigDecimal mediaValor, BigDecimal maiorValor) {
}
