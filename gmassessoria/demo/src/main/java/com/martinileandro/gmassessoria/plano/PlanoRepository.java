package com.martinileandro.gmassessoria.plano;

import com.martinileandro.gmassessoria.plano.dtos.PlanoResponseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PlanoRepository extends JpaRepository<Plano,Long> {

    @Query(value = "SELECT pl.*, (SELECT COUNT(co.id) FROM contratos AS co WHERE co.plano_id = pl.id) AS quantidade_alunos FROM planos AS pl where (:nome is null or nome like concat('%', :nome, '%')) and (:ciclo is null or ciclo = :ciclo);",nativeQuery = true)
    List<PlanoResponseProjection> findAllWithFilters(@Param("nome") String nome, @Param("ciclo") String ciclo);

    long countByStatus(PlanoStatus status);

    @Query(value = "select coalesce(avg(p.valor_base), 0) as valor_medio from planos as p", nativeQuery = true)
    BigDecimal getMediaValorBase();

    @Query(value = "select coalesce(max(p.valor_base), 0) as valor_maior from planos as p", nativeQuery = true)
    BigDecimal getMaiorValorBase();
}
