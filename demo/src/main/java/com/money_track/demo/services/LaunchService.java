package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.LaunchDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import com.money_track.demo.repositories.LaunchRepository;
import com.money_track.demo.repositories.UserRepository;
import com.money_track.demo.security.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaunchService {

    private final LaunchRepository launchRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public LaunchService(LaunchRepository launchRepository, TokenService tokenService, UserRepository userRepository) {
        this.launchRepository = launchRepository;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public List<LaunchDTO> findAllLaunches(String authHeader){
        User user = findUserByToken(authHeader);
        List<Launch> allLaunches = user.getLaunches();
        return allLaunches.stream().map(LaunchDTO::new).toList();
    }

    public LaunchDTO findLaunchById(String authHeader,Long id){
        User user = findUserByToken(authHeader);
        Launch launch = launchRepository.findById(id).orElseThrow(() -> new RuntimeException("erro"));
        if(user.getLaunches().contains(launch)){
            return new LaunchDTO(launch);
        }else{
            throw new RuntimeException("erro 2");
        }
    }


    public LaunchDTO createLaunch(Launch launch,String authHeader){
        User user = findUserByToken(authHeader);
        launch.setUser(user);
        launchRepository.save(launch);
        return new LaunchDTO(launch);
    }

    public LaunchDTO updateLaunch(Long id, Launch launch,String authHeader){
        User user = findUserByToken(authHeader);
        Launch updatedLaunch = launchRepository.findById(id).orElseThrow(() -> new RuntimeException("erro"));
        if(!user.getLaunches().contains(updatedLaunch)){
            throw new RuntimeException("erro");
        }
        updatedLaunch.setUser(user);
        updatedLaunch.setCategory(launch.getCategory());
        updatedLaunch.setDate(launch.getDate());
        updatedLaunch.setDescription(launch.getDescription());
        updatedLaunch.setValue(launch.getValue());
        launchRepository.save(updatedLaunch);
        return new LaunchDTO(updatedLaunch);
    }

    public void deleteLaunchById(Long id,String authHeader){
        User user = findUserByToken(authHeader);
        Launch deletedLaunch = launchRepository.findById(id).orElseThrow(() -> new RuntimeException("erro"));
        if(!user.getLaunches().contains(deletedLaunch)){
            throw new RuntimeException("erro");
        }
        try{
            launchRepository.deleteById(id);
        }catch (RuntimeException e){
            throw new RuntimeException("erro");
        }
    }

    public User findUserByToken(String authHeader){
        String token = authHeader.replace("Bearer ","");
        String email = tokenService.validateToken(token);
        return userRepository.findUserByEmail(email);
    }
}
