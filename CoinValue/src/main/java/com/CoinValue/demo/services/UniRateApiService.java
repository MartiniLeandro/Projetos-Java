package com.CoinValue.demo.services;

import com.CoinValue.demo.entities.DTO.AllCoinsDTO;
import com.CoinValue.demo.entities.DTO.ConvertAllCoinsDTO;
import com.CoinValue.demo.entities.DTO.ConvertAllCoinsRequestDTO;
import com.CoinValue.demo.entities.DTO.ConvertCoinDTO;
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


    public AllCoinsDTO getAllCoins(){
        return webClient.get().uri("/api/currencies?api_key={apiKey}", apiKey)
                .retrieve()
                .bodyToMono(AllCoinsDTO.class)
                .block();
    }

    public ConvertCoinDTO convertCoinToCoin(ConvertCoinDTO data){
        return webClient.get().uri("/api/convert?api_key={apiKey}&amount={amount}&from={from}&to={to}", apiKey, data.amount(), data.to(), data.from())
                .retrieve()
                .bodyToMono(ConvertCoinDTO.class)
                .block();
    }

    public ConvertAllCoinsDTO convertAllCoins(ConvertAllCoinsRequestDTO data){
        return webClient.get().uri("/api/rates?api_key={apiKey}&amount={amount}&from={base}",apiKey,data.amount(),data.base())
                .retrieve()
                .bodyToMono(ConvertAllCoinsDTO.class)
                .block();
    }

}
