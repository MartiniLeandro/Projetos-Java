package com.BarberHub.demo.repositories;

import com.BarberHub.demo.entities.Cliente;
import com.BarberHub.demo.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco,Long> {
}
