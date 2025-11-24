package com.money_track.demo.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.LaunchDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.exceptions.IsNotYoursException;
import com.money_track.demo.exceptions.NegativeNumberException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.CategoryRepository;
import com.money_track.demo.repositories.LaunchRepository;
import com.money_track.demo.repositories.UserRepository;
import com.money_track.demo.security.TokenService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LaunchService {

    private final LaunchRepository launchRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public LaunchService(LaunchRepository launchRepository, TokenService tokenService, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.launchRepository = launchRepository;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<LaunchDTO> findAllLaunches(String authHeader,Integer page, Integer size){
        Pageable pageable = PageRequest.of(page,size);
        User user = findUserByToken(authHeader);
        Page<Launch> allLaunches = launchRepository.findAllLaunchesByUser(pageable,user);
        return allLaunches.map(LaunchDTO::new);
    }

    public LaunchDTO findLaunchById(String authHeader,Long id){
        User user = findUserByToken(authHeader);
        Launch launch = launchRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe launch com este ID"));
        if(user.getLaunches().contains(launch)){
            return new LaunchDTO(launch);
        }else{
            throw new IsNotYoursException("este launch não pertence a você");
        }
    }

    public LaunchDTO createLaunch(Launch launch,String authHeader){
        User user = findUserByToken(authHeader);
        Category category = categoryRepository.findById(launch.getCategory().getId()).orElseThrow(() -> new NotFoundException("Não existe category com este ID"));
        launch.setCategory(category);
        launch.setUser(user);
        if(launch.getValue() < 0) throw new NegativeNumberException("Value não pode ser negativo");
        launchRepository.save(launch);
        return new LaunchDTO(launch);
    }

    public LaunchDTO updateLaunch(Long id, Launch launch,String authHeader){
        User user = findUserByToken(authHeader);
        Launch updatedLaunch = launchRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe launch com este ID"));
        if(!user.getLaunches().contains(updatedLaunch)){
            throw new IsNotYoursException("Este launch não pertence a você");
        }
        updatedLaunch.setUser(user);
        if(!categoryRepository.existsById(launch.getCategory().getId())) throw new NotFoundException("Não existe esta category");
        updatedLaunch.setCategory(categoryRepository.findById(launch.getCategory().getId()).orElseThrow(() -> new NotFoundException("Não existe category com este ID")));
        updatedLaunch.setDate(launch.getDate());
        updatedLaunch.setDescription(launch.getDescription());
        if(launch.getValue() < 0) throw new NegativeNumberException("Value não pode ser negativo");
        updatedLaunch.setValue(launch.getValue());
        launchRepository.save(updatedLaunch);
        return new LaunchDTO(updatedLaunch);
    }

    public void deleteLaunchById(Long id,String authHeader){
        User user = findUserByToken(authHeader);
        Launch deletedLaunch = launchRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe launch com este ID"));
        if(!user.getLaunches().contains(deletedLaunch)){
            throw new IsNotYoursException("Este launch não pertence a você");
        }
        try{
            launchRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Não existe launch com este ID");
        }
    }

    public List<LaunchDTO> filterLaunchByCategory(String categoryName, String authHeader){
        User user = findUserByToken(authHeader);
        List<Launch> launchesByCategory = user.getLaunches().stream().filter(launch -> launch.getCategory().getId().equals(categoryRepository.findByName(categoryName).getId())).toList();
        return launchesByCategory.stream().map(LaunchDTO::new).toList();
    }

    public List<LaunchDTO> filterLaunchByDate(LocalDate initialDate, LocalDate finalDate, String authHeader){
        User user = findUserByToken(authHeader);
        List<Launch> launchesByDate = launchRepository.findByUserAndDateBetween(user,initialDate,finalDate);
        return launchesByDate.stream().map(LaunchDTO::new).toList();

    }

    public List<LaunchDTO> filterByTypeValue(String authHeader, TypeValue typeValue){
        User user = findUserByToken(authHeader);
        List<Launch> launchesByTypeValue = launchRepository.findByUserAndCategory_TypeValue(user,typeValue);
        return launchesByTypeValue.stream().map(LaunchDTO::new).toList();
    }

    public User findUserByToken(String authHeader){
        String token = authHeader.replace("Bearer ","");
        if(token.isEmpty()) throw new JWTVerificationException("Token nulo");
        String email = tokenService.validateToken(token);
        return userRepository.findUserByEmail(email);
    }
}
