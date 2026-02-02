package com.BarberHub.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Barbearias")
public class Barbearia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nome cannot be null")
    private String nome;

    @NotBlank(message = "cnpj cannot be null")
    private String cnpj;

    @NotNull
    @OneToOne(mappedBy = "barbearia",cascade = CascadeType.ALL)
    private Endereco endereco;

    @NotBlank(message = "telefone cannot be null")
    private String telefone;

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL)
    private List<Barbeiro> barbeiros;

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL)
    private List<Servicos> servicos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
