package com.BarberHub.demo.entities.DTOS.agendamento;

import com.BarberHub.demo.entities.ENUMS.StatusCorte;

import java.time.LocalDateTime;

public interface AgendamentoResumeInterface {
    Long getId();
    LocalDateTime getHorario();
    String getCliente();
    String getServico();
    String getBarbeiro();
    StatusCorte getStatus();
}
