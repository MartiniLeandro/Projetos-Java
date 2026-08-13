package com.martinileandro.gmassessoria.contrato.dtos;

import com.martinileandro.gmassessoria.contrato.Contrato;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoResponseDTO(
        Long id,
        Long alunoId,
        Long planoId,
        LocalDate dataInicio,
        LocalDate dataFim,
        BigDecimal valorTotal
) {
    public ContratoResponseDTO(Contrato contrato){
        this(
                contrato.getId(), contrato.getAluno().getId(), contrato.getPlano().getId(), contrato.getDataInicio(), contrato.getDataFim(), contrato.getValorTotal()
        );
    }
}