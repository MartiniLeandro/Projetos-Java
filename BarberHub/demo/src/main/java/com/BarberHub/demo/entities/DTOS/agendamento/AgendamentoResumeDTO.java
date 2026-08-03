package com.BarberHub.demo.entities.DTOS.agendamento;

import com.BarberHub.demo.entities.Agendamento;

import java.time.format.DateTimeFormatter;

public record AgendamentoResumeDTO(Long id, String horario, String cliente, String servico, String barbeiro, String status){
    public AgendamentoResumeDTO(Agendamento data){
        this(
                data.getId(),
                data.getHoraInicial().format(DateTimeFormatter.ofPattern("HH:mm")),
                data.getCliente().getNome(),
                data.getServico().getNome(),
                data.getBarbeiro().getNome(),
                data.getStatus().name()
        );
    }

    public AgendamentoResumeDTO(AgendamentoResumeInterface data){
        this(
                data.getId(),
                data.getHorario().toString(),
                data.getCliente(),
                data.getServico(),
                data.getBarbeiro(),
                data.getStatus().name()
        );
    }
}
