package com.money_track.demo.repositories;

import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LaunchRepository extends JpaRepository<Launch,Long> {
    List<Launch> findByUserAndDateBetween(User user, LocalDate initialDate, LocalDate finalDate);
}
