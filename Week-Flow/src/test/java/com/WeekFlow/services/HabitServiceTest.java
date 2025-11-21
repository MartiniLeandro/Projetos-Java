package com.WeekFlow.services;

import com.WeekFlow.entities.ENUMS.Week;
import com.WeekFlow.entities.Habit;
import com.WeekFlow.repositories.HabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private HabitService habitService;

    private Habit h1,h2;

    @BeforeEach
    void setup(){
            h1 = Habit.builder().name("gym").daysWeek(List.of(Week.FRIDAY,Week.MONDAY,Week.SUNDAY)).recurrent(true).build();
            h2 = Habit.builder().name("school").daysWeek(List.of(Week.FRIDAY,Week.MONDAY,Week.SUNDAY)).recurrent(true).build();
    }
}
