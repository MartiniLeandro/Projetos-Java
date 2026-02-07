package com.BarberHub.demo.entities.DTOS;

import com.BarberHub.demo.entities.Endereco;

public record EnderecoDTO(String cep, String logradouro, String numero, String complemento, String bairro, String cidade) {
    public EnderecoDTO(Endereco endereco) {
        this(
                endereco.getCep(), endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(), endereco.getCidade()
        );
    }
}
