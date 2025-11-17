package com.WeekFlow.repositories;

import com.WeekFlow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    UserDetails findByEmail(String email);
    Boolean existsByEmail(String email);
    User findUserByEmail(String email);
}
