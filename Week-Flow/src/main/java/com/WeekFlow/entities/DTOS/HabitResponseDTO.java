package com.WeekFlow.entities.DTOS;

import com.WeekFlow.entities.ENUMS.Week;
import com.WeekFlow.entities.Habit;

import java.time.LocalTime;
import java.util.List;

public record HabitResponseDTO(String name, String description, LocalTime initialTime, LocalTime finalTime, List<Week> daysWeek, Boolean recurrent) {
    public HabitResponseDTO(Habit habit){
        this(
                habit.getName(), habit.getDescription(), habit.getInitialTime(), habit.getFinalTime(), habit.getDaysWeek(), habit.getRecurrent()
        );
    }
}
