package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Modifying
    @Query("update Order o set o.status = :status where o.id = :id")
    void setDelete(@Param("id") long id, @Param("status") Status status);
}