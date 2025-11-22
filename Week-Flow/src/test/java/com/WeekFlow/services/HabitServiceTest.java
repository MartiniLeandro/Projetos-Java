package com.WeekFlow.services;

import com.WeekFlow.entities.DTOS.HabitResponseDTO;
import com.WeekFlow.entities.ENUMS.Week;
import com.WeekFlow.entities.Habit;
import com.WeekFlow.entities.User;
import com.WeekFlow.repositories.HabitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private HabitService habitService;

    private Habit h1,h2;

    private User u1;

    @BeforeEach
    void setup(){
        h1 = Habit.builder().name("gym").daysWeek(List.of(Week.FRIDAY,Week.MONDAY,Week.SUNDAY)).recurrent(true).build();
        h2 = Habit.builder().name("school").daysWeek(List.of(Week.FRIDAY,Week.MONDAY,Week.SUNDAY)).recurrent(true).build();
        u1 = User.builder().name("user").email("user@email.com").password("user").build();
        u1.setHabits(List.of(h1,h2));
    }

    @Test
    void testFindAllHabits(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        List<HabitResponseDTO> allHabits = habitService.getAllHabits("fake-token");

        Assertions.assertEquals(2,allHabits.size());
        Assertions.assertEquals("gym",allHabits.getFirst().name());
    }
}
