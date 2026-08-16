package com.martinileandro.gmassessoria.plano;

import com.martinileandro.gmassessoria.plano.dtos.PlanoResponseProjection;
import com.martinileandro.gmassessoria.plano.dtos.RecebimentoPorPlanoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PlanoRepository extends JpaRepository<Plano,Long> {

    @Query(value = "SELECT pl.*, (SELECT COUNT(co.id) FROM contratos AS co WHERE co.plano_id = pl.id) AS quantidade_alunos FROM planos AS pl where (:nome is null or nome like concat('%', :nome, '%')) and (:ciclo is null or ciclo = :ciclo);",nativeQuery = true)
    List<PlanoResponseProjection> findAllWithFilters(@Param("nome") String nome, @Param("ciclo") String ciclo);

    long countByPlanoStatus(PlanoStatus status);

    @Query(value = "select coalesce(avg(p.valor_base), 0) as valor_medio from planos as p", nativeQuery = true)
    BigDecimal getMediaValorBase();

    @Query(value = "select coalesce(max(p.valor_base), 0) as valor_maior from planos as p", nativeQuery = true)
    BigDecimal getMaiorValorBase();

    @Query(value = "select pl.nome as plano, coalesce(sum(fa.valor_cobrado),0) as valor_recebido from planos as pl left join contratos as co on pl.id = co.plano_id left join faturas as fa on co.id = fa.contrato_id and fa.status = 'PAGO' and extract(month from fa.data_vencimento) = :mes and extract(year from fa.data_vencimento) = :ano group by pl.nome", nativeQuery = true)
    List<RecebimentoPorPlanoProjection> getRecebimentoPorPlano(@Param("mes") int mes, @Param("ano") int ano);
}
