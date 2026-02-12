package com.BarberHub.demo.repositories;

import com.BarberHub.demo.entities.Agendamento;
import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.Barbeiro;
import com.BarberHub.demo.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento,Long> {

    List<Agendamento> findAllByBarbearia(Barbearia barbearia);
    List<Agendamento> findAllByBarbeiro(Barbeiro barbeiro);
    List<Agendamento> findAllByCliente(Cliente cliente);
    List<Agendamento> findByBarbeariaIdAndHoraInicialBetween(Long barbeariaId, LocalDateTime inicio, LocalDateTime fim);
    List<Agendamento> findByBarbeiroIdAndHoraInicialBetween(Long idBarbeiro, LocalDateTime inicio, LocalDateTime fim);
}
