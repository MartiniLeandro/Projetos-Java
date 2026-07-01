package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.*;
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

import java.math.BigDecimal;
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
        if(!updatedLaunch.getUser().getId().equals(user.getId())){
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
        if(!deletedLaunch.getUser().getId().equals(user.getId())){
            throw new IsNotYoursException("Este launch não pertence a você");
        }
        launchRepository.delete(deletedLaunch);
    }

    public List<LaunchDTO> getLaunchesWithFilter(LaunchesFilterDTO data){ //utilizar pageable
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDate initialDate = data.initialDate() != null ? data.initialDate() : LocalDate.now().withDayOfMonth(1);
        LocalDate  finalDate = data.finalDate() != null ? data.finalDate() : LocalDate.now();
        List<Launch> launches = launchRepository.getLaunchesWithFilters(user.getId(), data.typeValue() != null ? data.typeValue().name() : null, data.categoryId(), initialDate, finalDate, data.description());
        return launches.stream().map(LaunchDTO::new).toList();
    }

    public TypeValuesDTO getTypeValuesByLaunches(List<LaunchDTO> launches){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Double revenue = 0.0 ,expense = 0.0;
        for(LaunchDTO launch : launches){
            if(launch.category().typeValue() == TypeValue.REVENUE){
                revenue +=  launch.value();
            }else {
                expense +=  launch.value();
            }
        }

        return new TypeValuesDTO(revenue,expense);
    }

    public List<CategoryTotalDTO> getCategoryTotalByDate(LocalDate initialDate, LocalDate finalDate){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDate inicialDate = initialDate != null ? initialDate : LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = finalDate != null ? finalDate : LocalDate.now();
        return launchRepository.getTotalMostExpensiveCategoriesByDate(user.getId(), inicialDate, endDate);
    }

    public BigDecimal getTotalByAllLaunches(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BigDecimal totalRevenue = launchRepository.getTotalRevenue(user.getId());
        BigDecimal totalExpense = launchRepository.getTotalExpense(user.getId());
        return totalRevenue.subtract(totalExpense);
    }

    public Double getTotalByPeriodLaunches(List<LaunchDTO> launches){
        Double revenueTotal = 0.0,  expenseTotal = 0.0;
        for(LaunchDTO launch : launches){
            if(launch.category().typeValue() == TypeValue.REVENUE) revenueTotal += launch.value();
            else expenseTotal += launch.value();
        }
        return revenueTotal - expenseTotal;
    }

    public LaunchesDataDTO getLaunchesData(LaunchesFilterDTO filter){ //falta mexer nos testes unitários
        List<LaunchDTO> launches = getLaunchesWithFilter(filter);
        TypeValuesDTO typeValues = getTypeValuesByLaunches(launches);
        List<CategoryTotalDTO> categoriesTotal = getCategoryTotalByDate(filter.initialDate(), filter.finalDate());
        Double totalByPeriodLaunches = getTotalByPeriodLaunches(launches);
        BigDecimal total = getTotalByAllLaunches();
        return new LaunchesDataDTO(launches, typeValues, totalByPeriodLaunches, total, categoriesTotal);
    }
}
