package com.martinileandro.gmassessoria.aluno.listagem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlunoListagemRepository extends JpaRepository<AlunoListagemView, Long>, JpaSpecificationExecutor<AlunoListagemView> {
}
