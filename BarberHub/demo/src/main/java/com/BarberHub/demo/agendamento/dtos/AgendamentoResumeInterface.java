package com.BarberHub.demo.agendamento.dtos;

import com.BarberHub.demo.agendamento.StatusCorte;

import java.time.LocalDateTime;

public interface AgendamentoResumeInterface {
    Long getId();
    LocalDateTime getHorario();
    String getCliente();
    String getServico();
    String getBarbeiro();
    StatusCorte getStatus();
}
