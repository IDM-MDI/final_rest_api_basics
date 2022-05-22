package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    @Modifying
    @Query("update User u set u.deleted = 1 where u.id = :id")
    long setDelete(@Param("id") Long id);

    List<User> findUsersByOrdersEmpty();

}
