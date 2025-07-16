package com.money_track.demo.controllers;

import com.money_track.demo.entities.DTO.LaunchDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.services.LaunchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/launches")
public class LaunchController {

    private final LaunchService launchService;

    public LaunchController(LaunchService launchService) {
        this.launchService = launchService;
    }

    @GetMapping
    public ResponseEntity<List<LaunchDTO>> findAllLaunches(){
        return ResponseEntity.ok().body(launchService.findAllLaunches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaunchDTO> findLaunchById(@PathVariable Long id){
        return ResponseEntity.ok().body(launchService.findLaunchById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<LaunchDTO> createLaunch(@RequestBody Launch launch){
        return ResponseEntity.ok().body(launchService.createLaunch(launch));
    }

    @PostMapping("update/{id}")
    public ResponseEntity<LaunchDTO> updateLaunch(@RequestBody Launch launch, @PathVariable Long id){
        return ResponseEntity.ok().body(launchService.updateLaunch(id,launch));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteLaunch(@PathVariable Long id){
        launchService.deleteLaunchById(id);
        return ResponseEntity.ok().build();
    }

}
