package com.martinileandro.gmassessoria.fatura;

import com.martinileandro.gmassessoria.fatura.dtos.HistoricoPagamentosProjection;
import com.martinileandro.gmassessoria.fatura.dtos.ListagemFaturasProjection;
import com.martinileandro.gmassessoria.financeiro.dtos.FluxoCaixaProjection;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FaturaRepository extends JpaRepository<Fatura,Long> {

    @Query(value = "select count(distinct co.id) from faturas fa left join contratos co on fa.contrato_id  = co.id left join planos pl on co.plano_id = pl.id where (CAST(:categoria AS text) is null or pl.categoria ilike concat('%', CAST(:categoria AS text), '%')) and fa.status = 'VENCIDA' and fa.data_vencimento < current_date", nativeQuery = true)
    Long contratosInadimplentesPorPlano(@Param("categoria") String categoria);

    @Query(value = "select coalesce(sum(fa.valor_cobrado),0) from faturas as fa where extract(month from fa.data_vencimento) = :mes and extract(year from fa.data_vencimento) = :ano", nativeQuery = true)
    BigDecimal getFaturamentoPrevistoMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query(value = "select coalesce(sum(fa.valor_cobrado),0) from faturas as fa where extract(month from fa.data_pagamento) = :mes and extract(year from fa.data_pagamento) = :ano and fa.status = 'PAGO'", nativeQuery = true)
    BigDecimal getFaturamentoRecebidoMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query(value = "select coalesce(sum(fa.valor_cobrado),0) from faturas as fa where extract(month from fa.data_vencimento) = :mes and extract(year from fa.data_vencimento) = :ano and (fa.data_pagamento is null or fa.data_pagamento > :ultimoDiaDoMes)", nativeQuery = true)
    BigDecimal getFaturamentoReceberMes(@Param("mes") int mes, @Param("ano") int ano, @Param("ultimoDiaDoMes") LocalDate ultimoDiaDoMes);

    @Query(value = "select coalesce(sum(fa.valor_cobrado),0) from faturas as fa where fa.status = 'VENCIDA'", nativeQuery = true)
    BigDecimal getInadimplenciaTotal();

    @Query(value = "SELECT EXTRACT(MONTH FROM s.mes_base) AS mes, EXTRACT(YEAR FROM s.mes_base) AS ano, COALESCE(prev.total_previsto, 0) AS previsto, COALESCE(rec.total_recebido, 0) AS recebido FROM generate_series(CAST(:dataInicial AS date), CAST(:dataFinal AS date), '1 month'::interval) AS s(mes_base) LEFT JOIN (SELECT EXTRACT(YEAR FROM data_vencimento) as ano, EXTRACT(MONTH FROM data_vencimento) as mes, SUM(valor_cobrado) as total_previsto FROM faturas GROUP BY 1, 2) prev ON prev.ano = EXTRACT(YEAR FROM s.mes_base) AND prev.mes = EXTRACT(MONTH FROM s.mes_base) LEFT JOIN (SELECT EXTRACT(YEAR FROM data_pagamento) as ano, EXTRACT(MONTH FROM data_pagamento) as mes, SUM(valor_cobrado) as total_recebido FROM faturas WHERE status = 'PAGO' GROUP BY 1, 2) rec ON rec.ano = EXTRACT(YEAR FROM s.mes_base) AND rec.mes = EXTRACT(MONTH FROM s.mes_base) ORDER BY ano ASC, mes ASC", nativeQuery = true)
    List<FluxoCaixaProjection> getFluxoCaixaMensal(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);

    @Query(value = "SELECT fa.id as id, fa.data_vencimento AS dataVencimento, fa.data_pagamento as dataPagamento, fa.numero_parcela AS numeroParcela, al.nome AS aluno, pl.nome AS plano, pl.ciclo AS ciclo, fa.valor_cobrado AS valorCobrado, fa.status AS status, fa.forma_pagamento AS formaPagamento FROM faturas AS fa INNER JOIN contratos AS co ON fa.contrato_id = co.id INNER JOIN planos AS pl ON co.plano_id = pl.id INNER JOIN alunos AS al ON co.aluno_id = al.id WHERE EXTRACT(MONTH FROM fa.data_vencimento) = :mes AND EXTRACT(YEAR FROM fa.data_vencimento) = :ano AND (CAST(:nomeAluno AS text) IS NULL OR al.nome ILIKE CONCAT('%', CAST(:nomeAluno AS text), '%')) AND (CAST(:statusFatura AS text) IS NULL OR fa.status = CAST(:statusFatura AS text))", nativeQuery = true)
    List<ListagemFaturasProjection> getListagemFaturas(@Param("mes") Integer mes, @Param("ano") Integer ano, @Param("nomeAluno") String nomeAluno, @Param("statusFatura") String statusFatura, Sort sort);

    @Query(value = "select fa.data_vencimento as dataVencimento, fa.data_pagamento as dataPagamento, fa.valor_cobrado as valorCobrado, fa.status as status, fa.forma_pagamento as formaPagamento from faturas as fa where fa.contrato_id = :contratoId", nativeQuery = true)
    List<HistoricoPagamentosProjection> getHistoricoPagamento(@Param("contratoId") Long contratoId);

    @Modifying
    @Query("UPDATE Fatura f SET f.status = 'VENCIDA' WHERE f.status = 'PENDENTE' AND f.dataVencimento < CURRENT_DATE")
    int atualizarFaturasVencidas();
}