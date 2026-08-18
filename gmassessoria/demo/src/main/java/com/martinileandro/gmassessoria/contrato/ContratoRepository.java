package com.martinileandro.gmassessoria.contrato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ContratoRepository extends JpaRepository<Contrato,Long> {

    @Query(value = "select count(co.id) as contratosAtivos from contratos co inner join planos pl on co.plano_id = pl.id where (:nomePlano is null or pl.nome ilike concat('%', :nomePlano, '%')) and co.status = 'ATIVO'", nativeQuery = true)
    Long contratosAtivosPorPlano(@Param("nomePlano") String nomePlano);

    @Query(value = "select count(co.id) as contratosAtivos from contratos co inner join planos pl on co.plano_id = pl.id where (:nomePlano is null or pl.nome ilike concat('%', :nomePlano, '%')) and co.status = 'ATIVO' and co.data_fim between current_date and (current_date + :diasAlerta)", nativeQuery = true)
    Long contratosProximosDoFimPorPlano(@Param("nomePlano") String nomePlano, @Param("diasAlerta") Integer diasAlerta);

}
