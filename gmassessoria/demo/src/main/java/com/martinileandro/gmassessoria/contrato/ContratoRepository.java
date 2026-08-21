package com.martinileandro.gmassessoria.contrato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ContratoRepository extends JpaRepository<Contrato,Long> {

    @Query(value = "select count(co.id) as contratosAtivos from contratos co inner join planos pl on co.plano_id = pl.id where (:categoria is null or pl.categoria ilike concat('%', :categoria, '%')) and co.status = 'ATIVO'", nativeQuery = true)
    Long contratosAtivosPorPlano(@Param("categoria") String categoria);

    @Query(value = "select count(co.id) as contratosAtivos from contratos co inner join planos pl on co.plano_id = pl.id where (:categoria is null or pl.nome ilike concat('%', :categoria, '%')) and co.status = 'ATIVO' and co.data_fim between current_date and (current_date + CAST(:diasAlerta AS int))", nativeQuery = true)
    Long contratosProximosDoFimPorPlano(@Param("categoria") String categoria, @Param("diasAlerta") Integer diasAlerta);

}
