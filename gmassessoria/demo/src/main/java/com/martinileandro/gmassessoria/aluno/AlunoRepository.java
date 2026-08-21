package com.martinileandro.gmassessoria.aluno;

import com.martinileandro.gmassessoria.aluno.dtos.AlunosContratosProximoFimListagemDTO;
import com.martinileandro.gmassessoria.aluno.dtos.AlunosContratosProximoFimProjection;
import com.martinileandro.gmassessoria.aluno.dtos.AlunosPorPlanoProjection;
import com.martinileandro.gmassessoria.aluno.dtos.EvolucaoAlunosProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno,Long> {

    @Query(value = "select count(distinct al.id) as totalAlunos from alunos al left join contratos as co on co.aluno_id = al.id left join planos as pl on co.plano_id = pl.id where (:categoria is null or pl.categoria ilike concat('%', :categoria, '%')) and al.status = 'ATIVO'", nativeQuery = true)
    Long contarAlunosAtivosPorPlano(@Param("categoria") String categoria);

    @Query(value = "select count(id) from alunos where EXTRACT(MONTH FROM data_cadastro) = :mes  and EXTRACT(YEAR FROM data_cadastro) = :ano", nativeQuery = true)
    Long contarNovosAlunos(@Param("mes") Integer mes, @Param("ano") Integer ano);

    @Query(value = "SELECT pl.categoria AS plano, COUNT(DISTINCT al.id) AS quantidade FROM alunos AS al INNER JOIN contratos AS co ON al.id = co.aluno_id INNER JOIN planos AS pl ON co.plano_id = pl.id WHERE al.status = 'ATIVO' AND (EXTRACT(YEAR FROM co.data_inicio) * 12 + EXTRACT(MONTH FROM co.data_inicio)) <= (:ano * 12 + :mes) AND (EXTRACT(YEAR FROM co.data_fim) * 12 + EXTRACT(MONTH FROM co.data_fim)) >= (:ano * 12 + :mes) GROUP BY pl.categoria", nativeQuery = true)
    List<AlunosPorPlanoProjection> contarAlunosAtivosAgrupadosPorPlano(@Param("mes") Integer mes, @Param("ano") Integer ano);

    @Query(value = "SELECT m.mes AS mes, (SELECT COUNT(id) FROM alunos WHERE EXTRACT(YEAR FROM data_cadastro) = :ano AND EXTRACT(MONTH FROM data_cadastro) = m.mes) AS novosAlunos, (SELECT COUNT(id) FROM alunos WHERE status = 'ATIVO' AND (EXTRACT(YEAR FROM data_cadastro) * 12 + EXTRACT(MONTH FROM data_cadastro)) <= (:ano * 12 + m.mes)) AS alunosAtivos FROM generate_series(1, :mesLimite) AS m(mes) ORDER BY m.mes ASC", nativeQuery = true)
    List<EvolucaoAlunosProjection> getEvolucaoAlunosPorAno(@Param("ano") Integer ano, @Param("mesLimite") Integer mesLimite);

    @Query(value = "SELECT al.imagem AS imagemAluno, al.nome AS nomeAluno, pl.nome AS nomePlano, co.data_inicio AS dataInicio, co.data_fim AS dataFim, (co.data_fim - CURRENT_DATE) AS tempoRestante FROM contratos AS co INNER JOIN alunos AS al ON co.aluno_id = al.id INNER JOIN planos AS pl ON co.plano_id = pl.id WHERE al.status = 'ATIVO' AND co.status = 'ATIVO' AND (EXTRACT(YEAR FROM co.data_inicio) * 12 + EXTRACT(MONTH FROM co.data_inicio)) <= (:ano * 12 + :mes) AND (EXTRACT(YEAR FROM co.data_fim) * 12 + EXTRACT(MONTH FROM co.data_fim)) >= (:ano * 12 + :mes) ORDER BY co.data_fim ASC LIMIT 6", nativeQuery = true)
    List<AlunosContratosProximoFimProjection> getListagemAlunosProximoFimDashboard(@Param("mes") Integer mes, @Param("ano") Integer ano);
}
