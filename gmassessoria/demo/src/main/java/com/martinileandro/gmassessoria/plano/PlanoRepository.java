package com.martinileandro.gmassessoria.plano;

import com.martinileandro.gmassessoria.plano.dtos.RecebimentoPorPlanoProjection;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PlanoRepository extends JpaRepository<Plano,Long> {

    @Query("SELECT p FROM Plano p WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', CAST(:nome AS String), '%'))) AND (:ciclo IS NULL OR p.ciclo = :ciclo) AND (:planoCategoria IS NULL OR p.planoCategoria = :planoCategoria) AND (:planoStatus IS NULL OR p.planoStatus = :planoStatus)")
    List<Plano> findAllWithFilters(@Param("nome") String nome, @Param("ciclo") String ciclo, @Param("planoCategoria") PlanoCategoria planoCategoria,@Param("planoStatus") PlanoStatus planoStatus, Sort sort);

    @Query(value = "select exists(select 1 from contratos where plano_id = :planoId) as possuiAlunos", nativeQuery = true)
    Boolean PlanoPossuiAlunoVinculado(@Param("planoId") Long planoId);

    long countByPlanoStatus(PlanoStatus status);

    @Query(value = "select coalesce(avg(p.valor_base), 0) as valor_medio from planos as p", nativeQuery = true)
    BigDecimal getMediaValorBase();

    @Query(value = "select coalesce(max(p.valor_base), 0) as valor_maior from planos as p", nativeQuery = true)
    BigDecimal getMaiorValorBase();

    @Query(value = "select pl.categoria as plano, coalesce(sum(fa.valor_cobrado),0) as valor_recebido from planos as pl left join contratos as co on pl.id = co.plano_id left join faturas as fa on co.id = fa.contrato_id and fa.status = 'PAGO' and extract(month from fa.data_vencimento) = :mes and extract(year from fa.data_vencimento) = :ano group by pl.categoria", nativeQuery = true)
    List<RecebimentoPorPlanoProjection> getRecebimentoPorPlano(@Param("mes") int mes, @Param("ano") int ano);
}
