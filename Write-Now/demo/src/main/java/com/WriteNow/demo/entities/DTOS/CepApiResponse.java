package com.WriteNow.demo.entities.DTOS;

public record CepApiResponse(String cep, String logradouro, String complemento, String bairro, String localidade, String uf, String estado, String regiao, String ddd) {
}
