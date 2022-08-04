package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o.* " +
            "FROM user_orders o " +
            "JOIN users u on o.user_id = u.id " +
            "GROUP BY u.id " +
            "ORDER BY SUM(o.price) DESC",
//            countQuery = "SELECT u.*,count(*) " +
//            "FROM user_orders o " +
//            "JOIN users u on o.user_id = u.id " +
//            "GROUP BY u.id " +
//            "ORDER BY SUM(o.price) DESC",
            nativeQuery = true)
    List<Order> getTop(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.status = :status where o.id = :id")
    void setDelete(@Param("id") long id, @Param("status") String status);
}