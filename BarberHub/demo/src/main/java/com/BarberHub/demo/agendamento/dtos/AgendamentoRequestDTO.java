package com.BarberHub.demo.agendamento.dtos;

import com.BarberHub.demo.agendamento.StatusCorte;

import java.time.LocalDateTime;

public record AgendamentoRequestDTO(Long idCliente, Long idBarbeiro, Long idBarbearia, Long idServico, LocalDateTime hora_inicial, StatusCorte statusCorte) {
}
