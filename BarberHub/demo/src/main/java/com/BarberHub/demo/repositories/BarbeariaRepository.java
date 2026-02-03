package com.BarberHub.demo.repositories;

import com.BarberHub.demo.entities.Barbearia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarbeariaRepository extends JpaRepository<Barbearia,Long> {

    Boolean existsByCnpj(String cnpj);
    List<Barbearia> findByNomeContainingIgnoreCase(String nome);
}
