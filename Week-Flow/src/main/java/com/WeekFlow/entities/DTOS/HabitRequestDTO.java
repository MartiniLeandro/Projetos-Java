package com.WeekFlow.entities.DTOS;

import com.WeekFlow.entities.ENUMS.Status;
import com.WeekFlow.entities.ENUMS.Week;
import com.WeekFlow.entities.Habit;
import com.WeekFlow.entities.User;

import java.time.LocalTime;
import java.util.List;

public record HabitRequestDTO(Long id, String name, String description, LocalTime initialTime, LocalTime finalTime, List<Week> daysWeek, Boolean recurrent) {
    public HabitRequestDTO(Habit habit){
        this(
                habit.getId(), habit.getName(), habit.getDescription(), habit.getInitialTime(), habit.getFinalTime(), habit.getDaysWeek(), habit.getRecurrent()
        );
    }
}
