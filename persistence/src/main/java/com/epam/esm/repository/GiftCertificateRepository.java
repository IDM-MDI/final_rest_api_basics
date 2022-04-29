package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateRepository extends CrudRepository<GiftCertificate, Long> {

}
