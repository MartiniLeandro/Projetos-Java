package com.martinileandro.gmassessoria.fatura;

import com.martinileandro.gmassessoria.financeiro.dtos.FluxoCaixaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FaturaRepository extends JpaRepository<Fatura,Long> {

    long countByStatusAndDataVencimentoBefore(FaturaStatus status, LocalDate data);

    @Query(value = "select coalesce(sum(fa.valor_cobrado),0) from faturas as fa where extract(month from fa.data_vencimento) = :mes and extract(year from fa.data_vencimento) = :ano", nativeQuery = true)
    BigDecimal getFaturamentoPrevistoMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query(value = "select coalesce(sum(fa.valor_cobrado),0) from faturas as fa where extract(month from fa.data_vencimento) = :mes and extract(year from fa.data_vencimento) = :ano and fa.status = 'PAGO'", nativeQuery = true)
    BigDecimal getFaturamentoRecebidoMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query(value = "select coalesce(sum(fa.valor_cobrado),0) from faturas as fa where fa.status = 'VENCIDA'", nativeQuery = true)
    BigDecimal getInadimplenciaTotal();

    @Query(value = "select extract(month from fa.data_vencimento) as mes, extract(year from fa.data_vencimento) as ano, coalesce(sum(fa.valor_cobrado), 0) as previsto, coalesce(sum(case when fa.status = 'PAGO' then fa.valor_cobrado else 0 end), 0) as recebido from faturas as fa where fa.data_vencimento >= :dataInicial and fa.data_vencimento <= :dataFinal group by extract(year from fa.data_vencimento), extract(month from fa.data_vencimento) order by ano asc, mes asc", nativeQuery = true)
    List<FluxoCaixaProjection> getFluxoCaixaMensal(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);
}
