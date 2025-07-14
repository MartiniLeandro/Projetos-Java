package com.money_track.demo.repositories;

import com.money_track.demo.entities.Launch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaunchRepository extends JpaRepository<Launch,Long> {
}
