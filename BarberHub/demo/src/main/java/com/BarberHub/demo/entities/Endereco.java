package com.BarberHub.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Enderecos")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "cep cannot be null")
    private String cep;

    @NotBlank(message = "logradouro cannot be null")
    private String logradouro;

    @NotBlank(message = "numero cannot be null")
    private String numero;

    @NotBlank(message = "complemento cannot be null")
    private String complemento;

    @NotBlank(message = "bairro cannot be null")
    private String bairro;

    @NotBlank(message = "cidade cannot be null")
    private String cidade;

    @NotBlank(message = "uf cannot be null")
    private String uf;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "barbearia_id")
    private Barbearia barbearia;

    public Endereco(String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String uf) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
    }
}
