package com.BarberHub.demo.entities.DTOS;

import com.BarberHub.demo.entities.Barbeiro;
import com.BarberHub.demo.entities.Endereco;
import com.BarberHub.demo.entities.Servico;

import java.util.List;

public record BarbeariaRequestDTO(String nome, String cnpj, String telefone, Endereco endereco, List<Barbeiro> barbeiros, List<Servico> servicos) {
}
