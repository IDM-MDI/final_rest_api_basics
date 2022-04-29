package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftTagRepository extends CrudRepository<GiftTag, Long> {

}
