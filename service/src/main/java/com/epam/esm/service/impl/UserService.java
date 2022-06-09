package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.builder.impl.ResponseDtoBuilder;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.StatusRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.EntityService;
import com.epam.esm.util.impl.UserModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Slf4j
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

    public ResponseDto<UserDto> saveWithResponse(UserDto dto) {
        UserDto user = mapper.toDto(save(dto));
        log.info("User: " + dto + " successfully added");
        return new ResponseDtoBuilder<UserDto>()
                .setCode(200)
                .setText("")
                .setContent(user)
                .build();
    }

    public DtoPage<UserDto> loginWithDtoPage(AuthenticationDto dto) {
        UserDto user = login(dto).orElseThrow(() -> {
            log.error("User not found");
            return new UsernameNotFoundException("");
        });
        log.info("User: " + user + " sign in");
        return new DtoPageBuilder<UserDto>()
                .setContent(List.of(user))
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

    public Optional<UserDto> login(AuthenticationDto authenticationDto) {
        return Optional.of(findUserByUsername(authenticationDto.getUsername()));
    }

    @Transactional
    public UserDto findUserByUsername(String username) {
        return mapper.toDto(repository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username - " + username + " not found")));
    }
}
