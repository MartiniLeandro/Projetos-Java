package com.CoinValue.demo.controllers;

import com.CoinValue.demo.entities.DTO.AllCoinsDTO;
import com.CoinValue.demo.entities.DTO.ConvertAllCoinsDTO;
import com.CoinValue.demo.entities.DTO.ConvertAllCoinsRequestDTO;
import com.CoinValue.demo.entities.DTO.ConvertCoinDTO;
import com.CoinValue.demo.services.UniRateApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UniRateApiController {

    private final UniRateApiService uniRateApiService;

    public UniRateApiController(UniRateApiService uniRateApiService) {
        this.uniRateApiService = uniRateApiService;
    }

    @GetMapping("/allCoins")
    public ResponseEntity<AllCoinsDTO> getAllCoins(){
        return ResponseEntity.ok().body(uniRateApiService.getAllCoins());
    }

    @GetMapping("/convert")
    public ResponseEntity<ConvertCoinDTO> convertCoin(@RequestParam Double amount, @RequestParam String baseCoin, @RequestParam String convertCoin){
        ConvertCoinDTO convertCoinDTO = ConvertCoinDTO.builder().amount(amount).to(baseCoin).from(convertCoin).build();
        return ResponseEntity.ok().body(uniRateApiService.convertCoinToCoin(convertCoinDTO));
    }

    @GetMapping("/convertToAll")
    public ResponseEntity<ConvertAllCoinsDTO> convertCoinToAllCoins(@RequestParam Double amount, @RequestParam String baseCoin){
        ConvertAllCoinsRequestDTO convertAllCoinsRequestDTO = ConvertAllCoinsRequestDTO.builder().amount(amount).base(baseCoin).build();
        return ResponseEntity.ok().body(uniRateApiService.convertAllCoins(convertAllCoinsRequestDTO));
    }

}
