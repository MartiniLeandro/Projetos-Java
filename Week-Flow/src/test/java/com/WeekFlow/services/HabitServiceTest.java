package com.WeekFlow.services;

import com.WeekFlow.entities.DTOS.HabitRequestDTO;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
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

    @Test
    void testGetHabitById(){
        h1.setId(1L);
        u1.setHabits(List.of(h1));
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(habitRepository.findById(anyLong())).thenReturn(Optional.ofNullable(h1));
        HabitResponseDTO habit = habitService.getHabitById("fake-token",1L);

        Assertions.assertNotNull(habit);
        Assertions.assertEquals("gym",habit.name());
    }

    @Test
    void testCreateHabit(){
        HabitRequestDTO newHabit = new HabitRequestDTO(h1);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(habitRepository.save(any(Habit.class))).thenReturn(h1);
        HabitResponseDTO habit = habitService.createHabit("fake-token", newHabit);

        Assertions.assertNotNull(habit);
        Assertions.assertEquals("gym",habit.name());

    }

    @Test
    void testUpdateHabit(){
        h1.setId(1L);
        HabitRequestDTO habitRequestDTO = new HabitRequestDTO(h2);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(habitRepository.findById(anyLong())).thenReturn(Optional.ofNullable(h1));
        when(habitRepository.save(any(Habit.class))).thenReturn(h1);
        HabitResponseDTO habit = habitService.updateHabit("fake-token", habitRequestDTO, 1L);

        Assertions.assertNotNull(habit);
        Assertions.assertEquals("gym",habit.name());
    }

    @Test
    void testDeleteHabit(){
        when(habitRepository.existsById(anyLong())).thenReturn(true);
        habitService.deleteHabit(1L);

        Mockito.verify(habitRepository).deleteById(1L);
    }
}
