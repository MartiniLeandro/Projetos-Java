package com.WriteNow.demo.services.API;

import com.WriteNow.demo.entities.DTOS.CepApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CepAPI {

    private final WebClient webClient;

    public CepAPI(WebClient.Builder builder){
        this.webClient = builder.baseUrl("https://viacep.com.br/ws").build();
    }

    public Mono<CepApiResponse> findInfosByCep(String cep){
        return webClient.get()
                .uri("/{cep}/json", cep)
                .retrieve().bodyToMono(CepApiResponse.class);
    }
}
