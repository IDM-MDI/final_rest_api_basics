package com.epam.esm.repository;

import com.epam.esm.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByNameIgnoreCase(String name);
}
