package com.martinileandro.gmassessoria.contrato.dtos;

import java.time.LocalDate;

public record ContratoListagemFilterDTO(
        String nomeAluno,
        String nomePlano,
        String cicloPlano,
        String statusContrato,
        String situacaoFinanceira,
        Integer minDiasRestantes,
        Integer maxDiasRestantes,
        LocalDate inicioMin,
        LocalDate inicioMax,
        LocalDate fimMin,
        LocalDate fimMax) {
}
