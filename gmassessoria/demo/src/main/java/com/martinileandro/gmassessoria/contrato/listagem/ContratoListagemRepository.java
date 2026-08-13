package com.martinileandro.gmassessoria.contrato.listagem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContratoListagemRepository extends JpaRepository<ContratoListagemView, Long>, JpaSpecificationExecutor<ContratoListagemView> {
}
