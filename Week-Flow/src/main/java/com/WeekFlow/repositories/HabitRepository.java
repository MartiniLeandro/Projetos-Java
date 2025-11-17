package com.WeekFlow.repositories;

import com.WeekFlow.entities.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitRepository extends JpaRepository<Habit,Long> {

}
