package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;
import java.util.List;
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Modifying
    @Query("SELECT o.user " +
            "FROM Order o " +
            "GROUP BY o.user.id " +
            "ORDER BY SUM(o.price) DESC")
    List<User> getTop();
    @Modifying
    @Query("update Order o set o.status = :status where o.id = :id")
    void setDelete(@Param("id") long id, @Param("status") Status status);
}