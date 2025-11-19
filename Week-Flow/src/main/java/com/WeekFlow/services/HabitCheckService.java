package com.WeekFlow.services;

import com.WeekFlow.entities.DTOS.HabitCheckResponseDTO;
import com.WeekFlow.entities.Habit;
import com.WeekFlow.entities.HabitCheck;
import com.WeekFlow.entities.User;
import com.WeekFlow.exceptions.IsNotYoursException;
import com.WeekFlow.exceptions.NotFoundException;
import com.WeekFlow.repositories.HabitCheckRepository;
import com.WeekFlow.repositories.HabitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HabitCheckService {

    private final HabitCheckRepository habitCheckRepository;
    private final UserService userService;
    private final HabitRepository habitRepository;

    public HabitCheckService(HabitCheckRepository habitCheckRepository, UserService userService, HabitRepository habitRepository) {
        this.habitCheckRepository = habitCheckRepository;
        this.userService = userService;
        this.habitRepository = habitRepository;
    }

    public HabitCheckResponseDTO markHabitForToday(Long habitId, String authHeader){
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new NotFoundException("Not exist habit with this ID"));
        User user = userService.findUserByToken(authHeader);
        if(!habit.getUser().getId().equals(user.getId())) throw new IsNotYoursException("This Habit is not your's");
        HabitCheck habitCheck = habitCheckRepository.findByHabitAndDate(habit, LocalDate.now()).orElse(null);
        if(habitCheck == null){
            habitCheck = HabitCheck.builder().habit(habit).date(LocalDate.now()).completed(true).build();

        }else {
            habitCheck.setCompleted(true);
        }
        habitCheckRepository.save(habitCheck);
        return new HabitCheckResponseDTO(habitCheck);
    }

    public HabitCheckResponseDTO unmarkHabitForToday(Long habitId, String authHeader){
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new NotFoundException("Not exist habit with this ID"));
        User user = userService.findUserByToken(authHeader);
        if(!habit.getUser().getId().equals(user.getId())) throw new IsNotYoursException("This Habit is not your's");
        HabitCheck habitCheck = habitCheckRepository.findByHabitAndDate(habit, LocalDate.now()).orElse(null);
        if(habitCheck == null){
            habitCheck = HabitCheck.builder().habit(habit).date(LocalDate.now()).completed(false).build();

        }else {
            habitCheck.setCompleted(false);
        }
        habitCheckRepository.save(habitCheck);
        return new HabitCheckResponseDTO(habitCheck);
    }

    public List<HabitCheckResponseDTO> getHabitsCheckByDate(LocalDate date, String authHeader){
        User user = userService.findUserByToken(authHeader);
        List<HabitCheck> habitsCheck = habitCheckRepository.findByHabitUserAndDate(user,date);
        return habitsCheck.stream().map(HabitCheckResponseDTO::new).toList();
    }

    public List<HabitCheckResponseDTO> getHabitsCheckBetweenDates(LocalDate start, LocalDate end, String authHeader){
        User user = userService.findUserByToken(authHeader);
        List<HabitCheck> habitsCheck = habitCheckRepository.findByHabitUserAndDateBetween(user,start,end);
        return habitsCheck.stream().map(HabitCheckResponseDTO::new).toList();
    }
}
