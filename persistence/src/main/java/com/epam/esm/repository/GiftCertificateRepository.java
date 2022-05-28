package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
    @Modifying
    @Query("update GiftCertificate gf set gf.deleted = 1 where gf.id = :id")
    void setDelete(@Param("id") Long id);

    Optional<GiftCertificate> findByName(String name);
    List<GiftCertificate> findByTagListIn(List<Tag> tagList);
}
