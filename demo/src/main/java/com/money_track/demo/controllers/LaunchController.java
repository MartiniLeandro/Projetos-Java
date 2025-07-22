package com.money_track.demo.controllers;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.LaunchDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.services.LaunchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user/launches")
public class LaunchController {

    private final LaunchService launchService;

    public LaunchController(LaunchService launchService) {
        this.launchService = launchService;
    }

    @GetMapping
    public ResponseEntity<List<LaunchDTO>> findAllLaunches(@RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok().body(launchService.findAllLaunches(authHeader));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaunchDTO> findLaunchById(@PathVariable @Valid Long id,@RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok().body(launchService.findLaunchById(authHeader,id));
    }

    @GetMapping("/filterByCategory")
    public ResponseEntity<List<LaunchDTO>> findByCategory(@RequestHeader("Authorization") String authHeader, @RequestParam Category category){
        return ResponseEntity.ok().body(launchService.filterLaunchByCategory(category,authHeader));
    }

    @GetMapping("/filterByDate")
    public ResponseEntity<List<LaunchDTO>> findByDate(@RequestHeader("Authorization") String authHeader,@RequestParam LocalDate initialDate,@RequestParam LocalDate finalDate){
        return ResponseEntity.ok().body(launchService.filterLaunchByDate(initialDate,finalDate, authHeader));
    }

    @GetMapping("filterByTypeValue")
    public ResponseEntity<List<LaunchDTO>> findByTypeValue(@RequestHeader("Authorization") String authHeader, @RequestParam TypeValue typeValue){
        return ResponseEntity.ok().body(launchService.filterByTypeValue(authHeader,typeValue));
    }

    @PostMapping("/create")
    public ResponseEntity<LaunchDTO> createLaunch(@RequestBody @Valid Launch launch,@RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok().body(launchService.createLaunch(launch,authHeader));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<LaunchDTO> updateLaunch(@RequestBody @Valid Launch launch, @PathVariable @Valid Long id,@RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok().body(launchService.updateLaunch(id,launch,authHeader));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteLaunch(@PathVariable @Valid Long id,@RequestHeader("Authorization") String authHeader){
        launchService.deleteLaunchById(id,authHeader);
        return ResponseEntity.noContent().build();
    }

}
