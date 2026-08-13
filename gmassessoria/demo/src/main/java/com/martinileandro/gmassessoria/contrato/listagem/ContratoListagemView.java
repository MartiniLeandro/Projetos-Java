package com.martinileandro.gmassessoria.contrato.listagem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import java.time.LocalDate;

@Entity
@Table(name = "vw_contratos_listagem")
@Immutable
@Getter
public class ContratoListagemView {

    @Id
    @Column(name = "contrato_id")
    private Long contratoId;

    @Column(name = "nome_aluno")
    private String nomeAluno;

    @Column(name = "imagem_aluno")
    private String imagemAluno;

    @Column(name = "nome_plano")
    private String nomePlano;

    @Column(name = "ciclo_plano")
    private String cicloPlano;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "tempo_restante")
    private Integer tempoRestante;

    @Column(name = "status_contrato")
    private String statusContrato;

    @Column(name = "situacao_financeira")
    private String situacaoFinanceira;
}