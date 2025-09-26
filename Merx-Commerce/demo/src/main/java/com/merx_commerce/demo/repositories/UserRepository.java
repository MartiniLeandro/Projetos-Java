package com.merx_commerce.demo.repositories;

import com.merx_commerce.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
