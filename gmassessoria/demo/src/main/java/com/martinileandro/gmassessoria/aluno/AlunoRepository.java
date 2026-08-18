package com.martinileandro.gmassessoria.aluno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlunoRepository extends JpaRepository<Aluno,Long> {

    @Query(value = "select count(distinct al.id) as totalAlunos from alunos al inner join contratos as co on co.aluno_id = al.id inner join planos as pl on co.plano_id = pl.id where (:nomePlano is null or pl.nome ilike concat('%', :nomePlano, '%')) and co.status = 'ATIVO'", nativeQuery = true)
    Long contarAlunosAtivosPorPlano(@Param("nomePlano") String nomePlano);
}
