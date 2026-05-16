package com.money_track.demo.controllers;

import com.money_track.demo.entities.DTO.*;
import com.money_track.demo.services.DashboardService;
import com.money_track.demo.services.LaunchService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/launches")
public class LaunchController {

    private final LaunchService launchService;
    private final DashboardService dashboardService;

    public LaunchController(LaunchService launchService, DashboardService dashboardService) {
        this.launchService = launchService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<LaunchDTO>> findAllLaunches(@RequestParam Integer page, @RequestParam Integer size){
        return ResponseEntity.ok().body(launchService.findAllLaunches(page,size));
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<LaunchDTO> findLaunchById(@PathVariable @Valid Long id){
        return ResponseEntity.ok().body(launchService.findLaunchById(id));
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

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardHome> dashboardBalances(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month){
        return ResponseEntity.ok().body(dashboardService.getDashboardData(year,month));
    }

    @GetMapping("/data")
    public ResponseEntity<LaunchesDataDTO> getLaunchesByFilters(@ModelAttribute LaunchesFilterDTO data){
        return ResponseEntity.ok().body(launchService.getLaunchesData(data));
    }
}
