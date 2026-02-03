package com.BarberHub.demo.repositories;

import com.BarberHub.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String email);

    Boolean existsByEmail(String email);
}
