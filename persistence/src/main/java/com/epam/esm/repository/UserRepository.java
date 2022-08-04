package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.status = :status where u.id = :id")
    void setDelete(@Param("id") long id, @Param("status") String status);


    List<User> findUsersByOrdersEmpty();
    @Query( value = "SELECT * FROM users WHERE status = ?1",
            countQuery = "SELECT count(*) FROM users WHERE status = ?1",
            nativeQuery = true)
    List<User> findByStatus(String status, Pageable pageable);


    Optional<User> findUserByUsername(String username);
}
