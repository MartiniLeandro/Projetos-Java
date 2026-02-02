package com.BarberHub.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nome cannot be null")
    private String nome;

    @NotBlank(message = "descricao cannot be null")
    private String descricao;

    @NotBlank(message = "preco cannot be null")
    private Double preco;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "barbearia_id")
    private Barbearia barbearia;
}
