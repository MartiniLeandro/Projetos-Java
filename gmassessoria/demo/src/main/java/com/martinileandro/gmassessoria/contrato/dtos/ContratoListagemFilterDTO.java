package com.martinileandro.gmassessoria.contrato.dtos;

import com.martinileandro.gmassessoria.plano.PlanoCategoria;

import java.time.LocalDate;

public record ContratoListagemFilterDTO(
        String nomeAluno,
        String nomePlano,
        PlanoCategoria planoCategoria,
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
