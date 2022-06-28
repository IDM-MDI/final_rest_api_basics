package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Modifying
    @Query("update User u set u.status = :status where u.id = :id")
    void setDelete(@Param("id") long id, @Param("status") Status status);


    List<User> findUsersByOrdersEmpty();

    List<User> findByStatus(Status status);

    Optional<User> findUserByUsername(String username);
}
