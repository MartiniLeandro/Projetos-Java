package com.WeekFlow.entities.DTOS;

import com.WeekFlow.entities.ENUMS.Status;
import com.WeekFlow.entities.ENUMS.Week;
import com.WeekFlow.entities.Habit;

import java.time.LocalTime;

public record HabitResposeDTO(String name, String description, Status status, LocalTime initialTime, LocalTime finalTime, Week daysWeek, Boolean recurrent) {
    public HabitResposeDTO(Habit habit){
        this(
                habit.getName(), habit.getDescription(), habit.getStatus(), habit.getInitialTime(), habit.getFinalTime(), habit.getDaysWeek(), habit.getRecurrent()
        );
    }
}
