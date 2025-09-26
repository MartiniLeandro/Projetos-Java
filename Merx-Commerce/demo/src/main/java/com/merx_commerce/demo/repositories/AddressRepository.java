package com.merx_commerce.demo.repositories;

import com.merx_commerce.demo.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
