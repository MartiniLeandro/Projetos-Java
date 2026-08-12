package com.martinileandro.gmassessoria.aluno;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode ser nulo")
    private String nome;

    private String telefone;

    private String imagem;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "o Status do aluno não pode ser nulo")
    private AlunoStatus status;
}
