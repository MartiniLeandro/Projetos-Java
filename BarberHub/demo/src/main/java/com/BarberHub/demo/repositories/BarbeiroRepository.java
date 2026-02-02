package com.BarberHub.demo.repositories;

import com.BarberHub.demo.entities.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarbeiroRepository extends JpaRepository<Barbeiro,Long> {
}
