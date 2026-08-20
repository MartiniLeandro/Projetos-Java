package com.martinileandro.gmassessoria.fatura;

import com.martinileandro.gmassessoria.fatura.dtos.ListagemFaturasProjection;
import com.martinileandro.gmassessoria.financeiro.dtos.FluxoCaixaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FaturaRepository extends JpaRepository<Fatura,Long> {

    @Query(value = "select count(distinct co.id) from faturas fa inner join contratos co on fa.contrato_id  = co.id inner join planos pl on co.plano_id = pl.id where (:nomePlano is null or pl.nome ilike concat('%', :nomePlano, '%')) and co.status = 'ATIVO' and fa.status = 'VENCIDA' and fa.data_vencimento < current_date", nativeQuery = true)
    Long contratosInadimplentesPorPlano(@Param("nomePlano") String nomePlano);

    @Query(value = "select coalesce(sum(fa.valor_cobrado),0) from faturas as fa where extract(month from fa.data_vencimento) = :mes and extract(year from fa.data_vencimento) = :ano", nativeQuery = true)
    BigDecimal getFaturamentoPrevistoMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query(value = "select coalesce(sum(fa.valor_cobrado),0) from faturas as fa where extract(month from fa.data_vencimento) = :mes and extract(year from fa.data_vencimento) = :ano and fa.status = 'PAGO'", nativeQuery = true)
    BigDecimal getFaturamentoRecebidoMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query(value = "select coalesce(sum(fa.valor_cobrado),0) from faturas as fa where fa.status = 'VENCIDA'", nativeQuery = true)
    BigDecimal getInadimplenciaTotal();

    @Query(value = "select extract(month from fa.data_vencimento) as mes, extract(year from fa.data_vencimento) as ano, coalesce(sum(fa.valor_cobrado), 0) as previsto, coalesce(sum(case when fa.status = 'PAGO' then fa.valor_cobrado else 0 end), 0) as recebido from faturas as fa where fa.data_vencimento >= :dataInicial and fa.data_vencimento <= :dataFinal group by extract(year from fa.data_vencimento), extract(month from fa.data_vencimento) order by ano asc, mes asc", nativeQuery = true)
    List<FluxoCaixaProjection> getFluxoCaixaMensal(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);

    @Query(value = "SELECT fa.data_vencimento AS dataVencimento, fa.numero_parcela AS numeroParcela, al.nome AS aluno, pl.nome AS plano, pl.ciclo AS ciclo, fa.valor_cobrado AS valorCobrado, fa.status AS status FROM faturas AS fa INNER JOIN contratos AS co ON fa.contrato_id = co.id INNER JOIN planos AS pl ON co.plano_id = pl.id INNER JOIN alunos AS al ON co.aluno_id = al.id WHERE EXTRACT(MONTH FROM fa.data_vencimento) = :mes AND EXTRACT(YEAR FROM fa.data_vencimento) = :ano AND (:nomeAluno IS NULL OR al.nome ILIKE CONCAT('%', :nomeAluno, '%')) AND (:statusFatura IS NULL OR fa.status = :statusFatura) ORDER BY fa.data_vencimento ASC", nativeQuery = true)
    List<ListagemFaturasProjection> getListagemFaturas(@Param("mes") Integer mes, @Param("ano") Integer ano, @Param("nomeAluno") String nomeAluno, @Param("statusFatura") String statusFatura);
}
