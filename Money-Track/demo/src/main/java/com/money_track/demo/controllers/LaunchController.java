package com.money_track.demo.controllers;

import com.money_track.demo.entities.DTO.*;
import com.money_track.demo.services.FinanceService;
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
    private final FinanceService financeService;

    public LaunchController(LaunchService launchService, FinanceService financeService) {
        this.launchService = launchService;
        this.financeService = financeService;
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
        return ResponseEntity.ok().body(financeService.getDashboardData(year,month));
    }

    @GetMapping("/teste")
    public ResponseEntity<List<LaunchDTO>> getLaunchesByFilters(@ModelAttribute LaunchesFilterDTO data){
        return ResponseEntity.ok().body(launchService.getLaunchesWithFilter(data));
    }

    @GetMapping("/teste2")
    public ResponseEntity<TypeValuesDTO>  getTypeValuesByFilters(@ModelAttribute LaunchesFilterDTO data){
        return ResponseEntity.ok().body(launchService.getTypeValuesByDate(data.initialDate(), data.finalDate()));
    }

    @GetMapping("/teste3")
    public ResponseEntity<List<CategoryTotalDTO>>  getCategoriesMostExpensives(@ModelAttribute LaunchesFilterDTO data){
        return ResponseEntity.ok().body(launchService.getCategoryTotalByDate(data.initialDate(), data.finalDate()));
    }
}
