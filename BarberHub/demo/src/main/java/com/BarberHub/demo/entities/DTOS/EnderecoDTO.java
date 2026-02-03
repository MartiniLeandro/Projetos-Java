package com.BarberHub.demo.entities.DTOS;

import com.BarberHub.demo.entities.Endereco;

public record EnderecoDTO(Long id, String cep, String logradouro, String numero, String complemento, String bairro, String cidade) {
    public EnderecoDTO(Endereco endereco) {
        this(
                endereco.getId(), endereco.getCep(), endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(), endereco.getCidade()
        );
    }
}
