package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.LaunchDTO;
import com.money_track.demo.entities.DTO.LaunchRequestDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.exceptions.IsNotYoursException;
import com.money_track.demo.exceptions.NegativeNumberException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.CategoryRepository;
import com.money_track.demo.repositories.LaunchRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LaunchService {

    private final LaunchRepository launchRepository;
    private final CategoryRepository categoryRepository;

    public LaunchService(LaunchRepository launchRepository, CategoryRepository categoryRepository) {
        this.launchRepository = launchRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<LaunchDTO> findAllLaunches(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page,size);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Launch> allLaunches = launchRepository.findAllLaunchesByUser(pageable,user);
        return allLaunches.map(LaunchDTO::new);
    }

    public LaunchDTO findLaunchById(Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Launch launch = launchRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe launch com este ID"));
        if(!user.getLaunches().contains(launch)) throw new IsNotYoursException("este launch não pertence a você");
        return new LaunchDTO(launch);
    }

    @Transactional
    public LaunchDTO createLaunch(LaunchRequestDTO launch){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(launch.value() < 0) throw new NegativeNumberException("Value não pode ser negativo");
        Category category = categoryRepository.findById(launch.categoryId()).orElseThrow(() -> new NotFoundException("Não existe category com este ID"));
        Launch newLaunch = Launch.builder().description(launch.description()).category(category).value(launch.value()).date(launch.date()).user(user).build();
        Launch savedLauncher = launchRepository.save(newLaunch);
        return new LaunchDTO(savedLauncher);
    }

    @Transactional
    public LaunchDTO updateLaunch(Long id, LaunchRequestDTO launch){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(launch.value() < 0) throw new NegativeNumberException("Value não pode ser negativo");
        Launch updatedLaunch = launchRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe launch com este ID"));
        if(!user.getLaunches().contains(updatedLaunch)){
            throw new IsNotYoursException("Este launch não pertence a você");
        }
        Category category = categoryRepository.findById(launch.categoryId()).orElseThrow(() -> new NotFoundException("Não existe category com este ID"));
        updatedLaunch.setCategory(category);
        updatedLaunch.setDate(launch.date());
        updatedLaunch.setDescription(launch.description());
        updatedLaunch.setValue(launch.value());
        Launch savedLaunch = launchRepository.save(updatedLaunch);
        return new LaunchDTO(savedLaunch);
    }

    @Transactional
    public void deleteLaunchById(Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Launch deletedLaunch = launchRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe launch com este ID"));
        if(!user.getLaunches().contains(deletedLaunch)){
            throw new IsNotYoursException("Este launch não pertence a você");
        }
        launchRepository.delete(deletedLaunch);
    }

    public List<LaunchDTO> filterLaunchByCategory(String categoryName){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryRepository.findByName(categoryName);
        List<Launch> launchesByCategory = user.getLaunches().stream().filter(launch -> launch.getCategory().getId().equals(category.getId())).toList();
        return launchesByCategory.stream().map(LaunchDTO::new).toList();
    }

    public List<LaunchDTO> filterLaunchByDate(LocalDate initialDate, LocalDate finalDate){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Launch> launchesByDate = launchRepository.findByUserAndDateBetween(user,initialDate,finalDate);
        return launchesByDate.stream().map(LaunchDTO::new).toList();

    }

    public List<LaunchDTO> filterByTypeValue(TypeValue typeValue){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Launch> launchesByTypeValue = launchRepository.findByUserAndCategory_TypeValue(user,typeValue);
        return launchesByTypeValue.stream().map(LaunchDTO::new).toList();
    }
}
