package com.BarberHub.demo.entities.DTOS.agendamento;

import com.BarberHub.demo.entities.ENUMS.StatusCorte;

import java.time.LocalDateTime;

public record AgendamentoRequestDTO(Long idCliente, Long idBarbeiro, Long idBarbearia, Long idServico, LocalDateTime hora_inicial, StatusCorte statusCorte) {
}
