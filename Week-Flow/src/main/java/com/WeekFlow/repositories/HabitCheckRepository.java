package com.WeekFlow.repositories;

import com.WeekFlow.entities.Habit;
import com.WeekFlow.entities.HabitCheck;
import com.WeekFlow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitCheckRepository extends JpaRepository<HabitCheck, Long> {
    Optional<HabitCheck> findByHabitAndDate(Habit habit, LocalDate date);
    List<HabitCheck> findByHabitUserAndDate(User user, LocalDate date);
    List<HabitCheck> findByHabitUserAndDateBetween(User user, LocalDate start, LocalDate end);

}
