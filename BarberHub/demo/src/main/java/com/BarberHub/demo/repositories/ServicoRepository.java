package com.BarberHub.demo.repositories;

import com.BarberHub.demo.entities.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico,Long> {
}
