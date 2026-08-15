package com.martinileandro.gmassessoria.fatura;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface FaturaRepository extends JpaRepository<Fatura,Long> {

    long countByStatusAndDataVencimentoBefore(FaturaStatus status, LocalDate data);
}
