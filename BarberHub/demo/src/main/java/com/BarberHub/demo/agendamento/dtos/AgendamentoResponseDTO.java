package com.BarberHub.demo.agendamento.dtos;

import com.BarberHub.demo.agendamento.Agendamento;
import com.BarberHub.demo.barbearia.dtos.BarbeariaNomeDTO;
import com.BarberHub.demo.barbeiro.dtos.BarbeiroNomeDTO;
import com.BarberHub.demo.cliente.dtos.ClienteNomeDTO;
import com.BarberHub.demo.servico.dtos.ServicoNomeDTO;
import com.BarberHub.demo.agendamento.StatusCorte;

import java.time.LocalDateTime;

public record AgendamentoResponseDTO(
        Long id,
        ClienteNomeDTO cliente,
        BarbeiroNomeDTO barbeiro,
        BarbeariaNomeDTO barbearia,
        ServicoNomeDTO servico,
        LocalDateTime hora_inicial,
        LocalDateTime hora_final,
        StatusCorte status) {
    public AgendamentoResponseDTO(Agendamento agendamento){
        this(
                agendamento.getId(),
                new ClienteNomeDTO(agendamento.getCliente()),
                new BarbeiroNomeDTO(agendamento.getBarbeiro()),
                new BarbeariaNomeDTO(agendamento.getBarbearia()),
                new ServicoNomeDTO(agendamento.getServico()),
                agendamento.getHoraInicial(),
                agendamento.getHoraInicial(),
                agendamento.getStatus()
        );
    }
}
