package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User,Long> {
    @Modifying
    @Query("update User u set u.deleted = 1 where u.id = :id")
    long setDelete(@Param("id") Long id);
}
