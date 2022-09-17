package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o " +
            "FROM Order o " +
            "JOIN User u on o.user = u " +
            "GROUP BY u.id " +
            "ORDER BY SUM(o.price) DESC")
    List<Order> getTop(Pageable pageable);
    @Query(value = "" +
            "SELECT t FROM Order o " +
            "JOIN o.gift g " +
            "JOIN g.tagList t " +
            "GROUP BY t.id " +
            "HAVING t.status = :status " +
            "ORDER BY count(t.id) DESC")
    List<Tag> getTopTagByStatus(@Param("status") String status, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.status = :status where o.id = :id")
    void setDelete(@Param("id") long id, @Param("status") String status);

    List<Order> findOrdersByUserAndStatus(User user, String status, Pageable pageable);
}