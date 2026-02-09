package com.BarberHub.demo.entities.DTOS.agendamento;

import com.BarberHub.demo.entities.Agendamento;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaNomeDTO;
import com.BarberHub.demo.entities.DTOS.barbeiro.BarbeiroNomeDTO;
import com.BarberHub.demo.entities.DTOS.cliente.ClienteNomeDTO;
import com.BarberHub.demo.entities.DTOS.servico.ServicoNomeDTO;
import com.BarberHub.demo.entities.ENUMS.StatusCorte;

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
                agendamento.getHora_inicial(),
                agendamento.getHora_final(),
                agendamento.getStatus()
        );
    }
}
