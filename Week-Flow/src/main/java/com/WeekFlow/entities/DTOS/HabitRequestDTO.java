package com.WeekFlow.entities.DTOS;

import com.WeekFlow.entities.ENUMS.Status;
import com.WeekFlow.entities.ENUMS.Week;
import com.WeekFlow.entities.User;

import java.time.LocalTime;

public record HabitRequestDTO(Long id, String name, String description, Status status, LocalTime initialTime, LocalTime finalTime, Week daysWeek, Boolean recurrent) {
}
