package com.martinileandro.gmassessoria.contrato.dtos;

import com.martinileandro.gmassessoria.contrato.FormaPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoRequestDTO(Long alunoId, Long planoId, LocalDate dataInicio, BigDecimal desconto, Integer numeroParcelas, FormaPagamento formaPagamento) {

}
