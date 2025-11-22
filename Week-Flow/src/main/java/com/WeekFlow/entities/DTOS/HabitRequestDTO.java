package com.WeekFlow.entities.DTOS;

import com.WeekFlow.entities.ENUMS.Status;
import com.WeekFlow.entities.ENUMS.Week;
import com.WeekFlow.entities.User;

import java.time.LocalTime;
import java.util.List;

public record HabitRequestDTO(Long id, String name, String description, Status status, LocalTime initialTime, LocalTime finalTime, List<Week> daysWeek, Boolean recurrent) {
}
