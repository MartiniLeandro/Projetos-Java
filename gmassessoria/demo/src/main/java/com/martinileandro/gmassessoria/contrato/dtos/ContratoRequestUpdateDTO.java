package com.martinileandro.gmassessoria.contrato.dtos;

import com.martinileandro.gmassessoria.contrato.ContratoStatus;
import com.martinileandro.gmassessoria.contrato.FormaPagamento;

public record ContratoRequestUpdateDTO(ContratoStatus status, FormaPagamento formaPagamento, String motivoDesconto) {
}
