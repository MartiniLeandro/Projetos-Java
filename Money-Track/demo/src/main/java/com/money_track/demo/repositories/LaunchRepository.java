package com.money_track.demo.repositories;

import com.money_track.demo.entities.DTO.CategoryTotalDTO;
import com.money_track.demo.entities.DTO.LaunchInterface;
import com.money_track.demo.entities.DTO.TypeValuesDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.TypeValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LaunchRepository extends JpaRepository<Launch,Long> {
    List<Launch> findByUserAndDateBetween(User user, LocalDate initialDate, LocalDate finalDate);
    List<Launch> findByUserAndCategory_TypeValue(User user, TypeValue typeValue);
    Page<Launch> findAllLaunchesByUser(Pageable pageable,User user);
    List<Launch> findByDescriptionContainingIgnoreCase(String description);

    @Query(value = "select sum(ln.value) from launches as ln inner join categories as ct on ln.category_id = ct.id where ln.user_id = :user_id and ct.type_value = 'REVENUE'", nativeQuery = true)
    BigDecimal getTotalRevenue(@Param("user_id") Long user_id);

    @Query(value = "select coalesce(sum(ln.value)) from launches as ln inner join categories as ct on ln.category_id = ct.id where ln.user_id = :user_id and ct.type_value = 'EXPENSE'", nativeQuery = true)
    BigDecimal getTotalExpense(@Param("user_id") Long user_id);

    @Query(value = "select coalesce(sum(ln.value)) from launches as ln join categories as ct on ln.category_id = ct.id where ln.user_id = :user_id and ct.type_value = 'REVENUE' and ln.date between :startDate and :endDate", nativeQuery = true)
    BigDecimal getTotalRevenueByMonth(@Param("user_id") Long user_id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select coalesce(sum(ln.value)) from launches as ln join categories as ct on ln.category_id = ct.id where ln.user_id = :user_id and ct.type_value = 'EXPENSE' and ln.date between :startDate and :endDate", nativeQuery = true)
    BigDecimal getTotalExpenseByMonth(@Param("user_id") Long user_id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select ct.name as name, sum(ln.value) as totalValue from launches as ln join categories as ct on ln.category_id = ct.id where ln.user_id = :user_id and ct.type_value = 'REVENUE' group by ct.name", nativeQuery = true)
    List<CategoryTotalDTO> getTotalRevenueByCategories(@Param("user_id") Long user_id);

    @Query(value = "select ct.name as name, sum(ln.value) as totalValue from launches as ln join categories as ct on ln.category_id = ct.id where ln.user_id = :user_id and ct.type_value = 'EXPENSE' group by ct.name", nativeQuery = true)
    List<CategoryTotalDTO> getTotalExpenseByCategories(@Param("user_id") Long user_id);

    @Query(value = "select ct.name as name, sum(ln.value) as totalValue from launches as ln join categories as ct on ln.category_id = ct.id where ln.user_id = :user_id and ct.type_value = 'REVENUE' and ln.date between :startDate and :endDate group by ct.name", nativeQuery = true)
    List<CategoryTotalDTO> getTotalRevenueByCategoriesByMonth(@Param("user_id") Long user_id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select ct.name as name, sum(ln.value) as totalValue from launches as ln join categories as ct on ln.category_id = ct.id where ln.user_id = :user_id and ct.type_value = 'EXPENSE' and ln.date between :startDate and :endDate group by ct.name", nativeQuery = true)
    List<CategoryTotalDTO> getTotalExpenseByCategoriesByMonth(@Param("user_id") Long user_id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select ln.id,ln.description,ct.name as categoryName, ct.icon as categoryIcon, ct.color as categoryColor, ct.type_value as type_value, ln.value,ln.date from launches as ln join categories as ct on ln.category_id = ct.id where ln.user_id = :user_id and ln.date between :startDate and :endDate order by ln.date desc limit 10",nativeQuery = true) //tive que utilizar interface projection por problema em transformar Date em LocalDate da query
    List<LaunchInterface> getLastLaunches(@Param("user_id") Long user_id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select l.* from launches as l join categories as c on l.category_id = c.id where l.user_id = :user_id and (:type_value is null or c.type_value = :type_value) and (:category_id is null or c.id = :category_id) and (cast(:startDate as date) is null or l.date >= :startDate) and (cast(:endDate as date) is null or l.date <= :endDate) and (:description is null or l.description ilike concat('%', :description, '%')) order by date desc", nativeQuery = true) //adicionar paginação
    List<Launch> getLaunchesWithFilters(@Param("user_id") Long user_id, @Param("type_value") String typeValue, @Param("category_id") Long category_id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("description") String description);

    @Query(value = "select coalesce(sum(case when c.type_value = 'REVENUE' then l.value end), 0) as revenue, coalesce(sum(case when c.type_value = 'EXPENSE' then l.value end), 0) as expense from launches as l join categories as c on l.category_id = c.id where l.user_id = :user_id and l.date between :startDate and :endDate", nativeQuery = true)
    TypeValuesDTO getTypeValuesByDate(@Param("user_id") Long user_Id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select c.name, coalesce(sum(l.value),0) from launches as l join categories as c on l.category_id = c.id where l.user_id = :user_id and l.date between :startDate and :endDate and c.type_value = 'EXPENSE' group by c.name order by sum(l.value) desc limit 5", nativeQuery = true)
    List<CategoryTotalDTO> getTotalMostExpensiveCategoriesByDate(@Param("user_id") Long user_Id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
