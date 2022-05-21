package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    @Modifying
    @Query("update Tag t set t.deleted = 1 where t.id = :id")
    long setDelete(@Param("id") Long id);

//    @Modifying
//    @Query("update Tag t set t.deleted = 1 where t.id = :id AND t.name = :name AND t.deleted = false")
//    List<Tag> findTagByParam(@Param("id") Long id,
//                             @Param("name") String name);
}
