package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.StatusRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.EntityService;
import com.epam.esm.util.impl.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class UserService implements EntityService<User,UserDto> {

    private final UserRepository repository;
    private final StatusRepository statusRepository;
    private final UserModelMapper mapper;

    @Autowired
    public UserService(UserRepository repository, StatusRepository statusRepository, UserModelMapper mapper) {
        this.repository = repository;
        this.statusRepository = statusRepository;
        this.mapper = mapper;
    }

    public DtoPage<UserDto> findAllPage(Integer page, Integer size, String sort) {
        return new DtoPageBuilder<UserDto>()
                .setContent(mapper.toDtoList(findAll(page,size,sort)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }

    public DtoPage<UserDto> findByIdWithPage(Long id) throws RepositoryException {

        return new DtoPageBuilder<UserDto>()
                .setContent(List.of(mapper.toDto(findById(id))))
                .build();
    }

    @Override
    public User save(UserDto dto) {
        return repository.save(mapper.toEntity(dto));
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public List<User> findAll(int page, int size, String sort) {
        return repository.findAll(PageRequest.of(page,size, Sort.by(sort))).toList();
    }

    @Override
    public User findById(long id) throws RepositoryException {
        return repository.findById(id)
                .orElseThrow(() -> new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString()));
    }

    @Override
    public List<User> findActive() {
        return findByStatus(DELETED.name());
    }

    @Override
    public List<User> findDeleted() {
        return findByStatus(ACTIVE.name());
    }

    @Override
    public void delete(long id) {
        repository.setDelete(id,statusRepository.findByNameIgnoreCase(DELETED.name()));
    }

    @Override
    public List<User> findByStatus(String statusName) {
        return repository.findByStatus(statusRepository.findByNameIgnoreCase(statusName));
    }

    public Optional<User> findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }
}
