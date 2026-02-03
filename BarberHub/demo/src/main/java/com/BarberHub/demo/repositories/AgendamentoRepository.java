package com.BarberHub.demo.repositories;

import com.BarberHub.demo.entities.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento,Long> {
}
