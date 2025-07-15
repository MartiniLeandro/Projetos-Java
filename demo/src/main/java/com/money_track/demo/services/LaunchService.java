package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.LaunchDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.repositories.LaunchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaunchService {
    private final LaunchRepository launchRepository;

    public LaunchService(LaunchRepository launchRepository) {
        this.launchRepository = launchRepository;
    }

    public List<LaunchDTO> findAllLaunches(){
        List<Launch> allLaunches = launchRepository.findAll();
        return allLaunches.stream().map(LaunchDTO::new).toList();
    }

    public LaunchDTO findLaunchById(Long id){
        Launch launch = launchRepository.findById(id).orElseThrow(() -> new RuntimeException("erro"));
        return new LaunchDTO(launch);
    }


    public LaunchDTO createLaunch(Launch launch){
        launchRepository.save(launch);
        return new LaunchDTO(launch);
    }

    public LaunchDTO updateLaunch(Long id, Launch launch){
        Launch updatedLaunch = launchRepository.findById(id).orElseThrow(() -> new RuntimeException("erro"));
        updatedLaunch.setCategory(launch.getCategory());
        updatedLaunch.setDate(launch.getDate());
        updatedLaunch.setDescription(launch.getDescription());
        updatedLaunch.setUser(launch.getUser());
        updatedLaunch.setValue(launch.getValue());
        launchRepository.save(updatedLaunch);
        return new LaunchDTO(updatedLaunch);
    }

    public void deleteLaunchById(Long id){
        launchRepository.deleteById(id);
    }
}
