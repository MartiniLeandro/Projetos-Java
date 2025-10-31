package com.CoinValue.demo.controllers;

import com.CoinValue.demo.entities.DTO.AllCoinsResponseDTO;
import com.CoinValue.demo.services.UniRateApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UniRateApiController {

    private final UniRateApiService uniRateApiService;

    public UniRateApiController(UniRateApiService uniRateApiService) {
        this.uniRateApiService = uniRateApiService;
    }

    @GetMapping("/allCoins")
    public ResponseEntity<AllCoinsResponseDTO> getAllCoins(){
        return ResponseEntity.ok().body(uniRateApiService.getAllCoins());
    }
}
