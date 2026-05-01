package com.money_track.demo.repositories;

import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.TypeValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LaunchRepository extends JpaRepository<Launch,Long> {
    List<Launch> findByUserAndDateBetween(User user, LocalDate initialDate, LocalDate finalDate);
    List<Launch> findByUserAndCategory_TypeValue(User user, TypeValue typeValue);
    Page<Launch> findAllLaunchesByUser(Pageable pageable,User user);

    @Query(value = "select sum(value) from launches as ln inner join categories as ct on ln.category_id = ct.id where ct.type_value = 'REVENUE'", nativeQuery = true)
    BigDecimal getTotalRevenue(Long userId);

    @Query(value = "select sum(value) from launches as ln inner join categories as ct on ln.category_id = ct.id where ct.type_value = 'EXPENSE'", nativeQuery = true)
    BigDecimal getTotalExpense(Long userId);
}
