package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
//    List<GiftCertificate> findGiftCertificatesByIdAndNameAndDescriptionAndPriceAndDurationAndCreate_dateAndUpdate_date(
//            long id, String name, String description,
//            BigDecimal price, int duration,
//            LocalDateTime create, LocalDateTime update);

    @Modifying
    @Query("update GiftCertificate gf set gf.deleted = 1 where gf.id = :id")
    long setDelete(@Param("id") Long id);

    GiftCertificate findByName(String name);
}
