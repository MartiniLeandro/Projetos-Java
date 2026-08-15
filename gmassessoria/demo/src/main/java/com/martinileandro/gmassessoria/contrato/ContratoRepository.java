package com.martinileandro.gmassessoria.contrato;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ContratoRepository extends JpaRepository<Contrato,Long> {

    long countByStatus(ContratoStatus status);
    long countByStatusAndDataFimBetween(ContratoStatus status, LocalDate inicio, LocalDate fim);
}
