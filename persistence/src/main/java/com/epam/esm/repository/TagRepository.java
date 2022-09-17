package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    @Modifying(clearAutomatically = true)
    @Query("update Tag t set t.status = :status where t.id = :id")
    void setDelete(@Param("id") long id, @Param("status") String status);

    List<Tag> findTagsByStatus(String status, Pageable pageable);
    @Query(
            value = "SELECT t FROM Tag t ORDER BY function('RAND')")
    List<Tag> findRandomTag(Pageable pageable);
    boolean existsByName(String name);

}
