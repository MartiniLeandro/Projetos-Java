package com.CoinValue.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExchangeRateApiService {

    private final WebClient webClient;

    public ExchangeRateApiService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.exchangerate.host/")
                .build();
    }
}
