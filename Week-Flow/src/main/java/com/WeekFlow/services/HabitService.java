package com.WeekFlow.services;

import com.WeekFlow.entities.DTOS.HabitRequestDTO;
import com.WeekFlow.entities.DTOS.HabitResponseDTO;
import com.WeekFlow.entities.Habit;
import com.WeekFlow.entities.User;
import com.WeekFlow.exceptions.IsNotYoursException;
import com.WeekFlow.exceptions.NotFoundException;
import com.WeekFlow.repositories.HabitRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserService userService;

    public HabitService(HabitRepository habitRepository, UserService userService) {
        this.habitRepository = habitRepository;
        this.userService = userService;
    }

    public List<HabitResponseDTO> getAllHabits(String authHeader){
        User user = userService.findUserByToken(authHeader);
        List<Habit> allHabits = user.getHabits();
        return allHabits.stream().map(HabitResponseDTO::new).toList();
    }

    public HabitResponseDTO getHabitById(String authHeader, Long id){
        User user = userService.findUserByToken(authHeader);
        Habit habit = habitRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist habit with this ID"));
        if(!user.getHabits().contains(habit)) throw new IsNotYoursException("This habit is not your's");
        return new HabitResponseDTO(habit);
    }

    public HabitResponseDTO createHabit(String authHeader, HabitRequestDTO habit){
        User user = userService.findUserByToken(authHeader);
        Habit newHabit = new Habit(habit);
        newHabit.setUser(user);
        habitRepository.save(newHabit);
        return new HabitResponseDTO(newHabit);
    }

    public HabitResponseDTO updateHabit(String authHeader, HabitRequestDTO habit, Long id){
        User user = userService.findUserByToken(authHeader);
        Habit updatedHabit = habitRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist habit with this ID"));
        if(!user.getHabits().contains(updatedHabit)) throw new IsNotYoursException("This habit is not your's");
        BeanUtils.copyProperties(updatedHabit, habit, "id");
        habitRepository.save(updatedHabit);
        return new HabitResponseDTO(updatedHabit);
    }

    public void deleteHabit(Long id){
        if(habitRepository.existsById(id)){
            habitRepository.deleteById(id);
        }else{
            throw new NotFoundException("Not exist habit with this ID");
        }
    }
}
