package com.CoinValue.demo.services;

import com.CoinValue.demo.entities.DTO.AllCoinsResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UniRateApiService {

    private final WebClient webClient;

    @Value("${api.key}")
    private String apiKey;

    public UniRateApiService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.unirateapi.com")
                .build();
    }


    public AllCoinsResponseDTO getAllCoins(){
        return webClient.get().uri("/api/rates?api_key={apiKey}", apiKey)
                .retrieve()
                .bodyToMono(AllCoinsResponseDTO.class)
                .block();
    }

}
