package com.money_track.demo.controllers;

import com.money_track.demo.entities.DTO.*;
import com.money_track.demo.services.DashboardService;
import com.money_track.demo.services.ExportLaunchesService;
import com.money_track.demo.services.LaunchService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/launches")
public class LaunchController {

    private final LaunchService launchService;
    private final DashboardService dashboardService;
    private final ExportLaunchesService exportLaunchesService;

    public LaunchController(LaunchService launchService, DashboardService dashboardService, ExportLaunchesService exportLaunchesService) {
        this.launchService = launchService;
        this.dashboardService = dashboardService;
        this.exportLaunchesService = exportLaunchesService;
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

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(@ModelAttribute LaunchesFilterDTO data){
        byte[] excelBytes = exportLaunchesService.CreateExcelFile(data);

        if (excelBytes.length == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        headers.setContentDispositionFormData("attachment", "launches_report.xlsx");

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

    }
}
