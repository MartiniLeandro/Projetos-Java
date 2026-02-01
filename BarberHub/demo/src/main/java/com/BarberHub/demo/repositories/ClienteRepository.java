package com.BarberHub.demo.repositories;

import com.BarberHub.demo.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
