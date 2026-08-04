package com.BarberHub.demo.barbearia.dtos;

import com.BarberHub.demo.barbearia.Endereco;

public record EnderecoDTO(String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String uf) {
    public EnderecoDTO(Endereco endereco) {
        this(
                endereco.getCep(), endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(), endereco.getCidade(), endereco.getUf()
        );
    }
}
