package com.BarberHub.demo.entities.DTOS.agendamento;

import java.time.LocalDateTime;

public record AgendamentoRequestDTO(Long idCliente, Long idBarbeiro, Long idBarbearia, Long idServico, LocalDateTime hora_inicial) {
}
