package com.money_track.demo.repositories;

import com.money_track.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;


public interface UserRepository extends JpaRepository<User,Long> {
    UserDetails findByEmail(String email);
    User findUserByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
