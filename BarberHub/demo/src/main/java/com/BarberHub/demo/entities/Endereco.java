package com.BarberHub.demo.entities;

import com.BarberHub.demo.entities.DTOS.endereco.EnderecoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
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

    public Endereco(EnderecoDTO enderecoDTO) {
        this.cep = enderecoDTO.cep();
        this.logradouro = enderecoDTO.logradouro();
        this.numero = enderecoDTO.numero();
        this.complemento = enderecoDTO.complemento();
        this.bairro = enderecoDTO.bairro();
        this.cidade = enderecoDTO.cidade();
        this.uf = enderecoDTO.uf();
    }
}
