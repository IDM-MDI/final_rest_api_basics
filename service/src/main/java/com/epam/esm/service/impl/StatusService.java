package com.epam.esm.service.impl;

import com.epam.esm.dto.StatusDto;
import com.epam.esm.entity.Status;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.StatusRepository;
import com.epam.esm.util.impl.StatusModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_STATUS_NOT_FOUND;

@Service
public class StatusService {
    private final StatusRepository repository;
    private final StatusModelMapper mapper;

    @Autowired
    public StatusService(StatusRepository repository, StatusModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public StatusDto findStatusDto(String name) throws RepositoryException {
        return mapper.toDto(findStatus(name));
    }
    public Status findStatus(String name) throws RepositoryException {
        return repository.findByNameIgnoreCase(name)
                         .orElseThrow(() -> new RepositoryException(REPOSITORY_STATUS_NOT_FOUND.toString()));
    }
}
