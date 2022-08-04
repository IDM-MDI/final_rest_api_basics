package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftTagRepository extends JpaRepository<GiftTag, Long> {
    @Modifying(clearAutomatically = true)
    @Query("update GiftTag gt set gt.status = :status where gt.gift.id = :id")
    void setDeleteByGift(@Param("id") long id, @Param("status") String status);
}
