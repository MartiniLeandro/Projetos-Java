package com.BarberHub.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Endereco {

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
}
