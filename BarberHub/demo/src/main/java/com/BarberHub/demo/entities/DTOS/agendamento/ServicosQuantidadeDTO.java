package com.BarberHub.demo.entities.DTOS.agendamento;

public record ServicosQuantidadeDTO(String name, Long quantity) {
    public ServicosQuantidadeDTO(ServicosQuantidadeInterface data){
        this(
                data.getNome(), data.getQuantidade()
        );
    }

}
