package com.WeekFlow.entities.DTOS;

import com.WeekFlow.entities.Habit;
import com.WeekFlow.entities.HabitCheck;

import java.time.LocalDate;

public record HabitCheckResponseDTO(Long id, Habit habit, LocalDate date, Boolean completed) {
    public HabitCheckResponseDTO(HabitCheck habitCheck){
        this(habitCheck.getId(),habitCheck.getHabit(),habitCheck.getDate(),habitCheck.getCompleted());
    }
}
