package com.martinileandro.gmassessoria.aluno;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

@Getter
@Immutable
@Entity
@Table(name = "vw_alunos_listagem")
public class AlunoListagemView {

    @Id
    @Column(name = "id_aluno")
    private Long id;

    @Column(name = "imagem_aluno")
    private String imagem;

    @Column(name = "nome_aluno")
    private String nome;

    @Column(name = "nome_plano")
    private String plano;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "tempo_restante")
    private Integer tempoRestante;

    @Column(name = "status_financeiro")
    private String statusFinanceiro;

    @Column(name = "status_aluno")
    private String statusAluno;
}
