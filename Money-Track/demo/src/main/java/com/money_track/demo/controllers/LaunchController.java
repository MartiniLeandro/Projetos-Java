package com.money_track.demo.controllers;

import com.money_track.demo.entities.DTO.LaunchDTO;
import com.money_track.demo.entities.DTO.LaunchRequestDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.services.LaunchService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<LaunchDTO>> findAllLaunches(@RequestParam Integer page, @RequestParam Integer size){
        return ResponseEntity.ok().body(launchService.findAllLaunches(page,size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaunchDTO> findLaunchById(@PathVariable @Valid Long id){
        return ResponseEntity.ok().body(launchService.findLaunchById(id));
    }

    @GetMapping("/filterByCategory")
    public ResponseEntity<List<LaunchDTO>> findByCategory(@RequestParam String category){
        return ResponseEntity.ok().body(launchService.filterLaunchByCategory(category));
    }

    @GetMapping("/filterByDate")
    public ResponseEntity<List<LaunchDTO>> findByDate(@RequestParam LocalDate initialDate,@RequestParam LocalDate finalDate){
        return ResponseEntity.ok().body(launchService.filterLaunchByDate(initialDate,finalDate));
    }

    @GetMapping("filterByTypeValue")
    public ResponseEntity<List<LaunchDTO>> findByTypeValue(@RequestParam TypeValue typeValue){
        return ResponseEntity.ok().body(launchService.filterByTypeValue(typeValue));
    }

    @PostMapping("/create")
    public ResponseEntity<LaunchDTO> createLaunch(@RequestBody @Valid LaunchRequestDTO launch){
        return ResponseEntity.ok().body(launchService.createLaunch(launch));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<LaunchDTO> updateLaunch(@RequestBody @Valid LaunchRequestDTO launch, @PathVariable @Valid Long id){
        return ResponseEntity.ok().body(launchService.updateLaunch(id,launch));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteLaunch(@PathVariable @Valid Long id){
        launchService.deleteLaunchById(id);
        return ResponseEntity.noContent().build();
    }

}
