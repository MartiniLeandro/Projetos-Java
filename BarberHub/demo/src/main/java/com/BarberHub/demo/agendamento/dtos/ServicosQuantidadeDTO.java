package com.BarberHub.demo.agendamento.dtos;

public record ServicosQuantidadeDTO(String name, Long quantity) {
    public ServicosQuantidadeDTO(ServicosQuantidadeInterface data){
        this(
                data.getNome(), data.getQuantidade()
        );
    }

}
