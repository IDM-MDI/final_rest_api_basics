package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.*;
import com.epam.esm.entity.*;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.EntityService;
import com.epam.esm.service.ResponseService;
import com.epam.esm.util.HashGenerator;
import com.epam.esm.util.impl.RoleModelMapper;
import com.epam.esm.util.impl.StatusModelMapper;
import com.epam.esm.util.impl.UserModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static com.epam.esm.exception.RepositoryExceptionCode.*;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Slf4j
@Profile("prod")
public class UserService implements EntityService<User,UserDto> {

    public static final String USER = "User ";
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final UserModelMapper mapper;
    private final RoleModelMapper roleMapper;
    private final ResponseService responseService;
    private final StatusService statusService;
    private final StatusModelMapper statusMapper;
    private final OrderRepository orderRepository;
    @Autowired
    public UserService(UserRepository repository,
                       StatusService statusService,
                       RoleRepository roleRepository,
                       UserModelMapper mapper,
                       RoleModelMapper roleMapper,
                       ResponseService responseService, StatusModelMapper statusMapper, OrderRepository orderRepository) {
        this.repository = repository;
        this.statusService = statusService;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
        this.roleMapper = roleMapper;
        this.responseService = responseService;
        this.statusMapper = statusMapper;
        this.orderRepository = orderRepository;
    }

    public DtoPage<UserDto> findAllPageWithDtoPage(Integer page, Integer size, String sort) {
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(
                        USER + PAGE + "page " + page + ", size" + size + ", sort" + sort
                ))
                .setContent(mapper.toDtoList(findAll(page,size,sort)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }

    public DtoPage<UserDto> findByIdWithDtoPage(Long id) throws RepositoryException {
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(USER + FOUND_BY_ID))
                .setContent(List.of(mapper.toDto(findById(id))))
                .build();
    }

    public DtoPage<UserDto> saveWithDtoPage(UserDto dto) throws RepositoryException {
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.createdResponse(USER + CREATED))
                .setContent(List.of(mapper.toDto(save(dto))))
                .build();
    }

    public DtoPage<UserDto> loginWithDtoPage(AuthenticationDto dto) {
        UserDto user = login(dto).orElseThrow(() -> {
            log.error("User not found");
            return new UsernameNotFoundException("");
        });
        log.info("User: " + user + " sign in");
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(USER + LOGGED_IN))
                .setContent(List.of(user))
                .build();
    }
    @Override
    public User save(UserDto dto) throws RepositoryException {
        setDefaultUser(dto);
        User user = mapper.toEntity(dto);
        User result = repository.save(user);
        log.info("User: " + dto + " successfully added");
        return result;
    }

    @Override
    public User update(UserDto dto) {
        UserDto userByUsername = findUserByUsername(dto.getUsername());
        User user = mapper.toEntity(userByUsername);
        user.setPassword(HashGenerator.generateHash(dto.getPassword()));
        return repository.save(user);
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
    public List<User> findByParam(UserDto dto) throws RepositoryException {
        List<User> users = repository.findAll(Example.of(mapper.toEntity(dto)));
        if(users.size() == 0) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_EXCEPTION.toString());
        }
        return users;
    }

    @Override
    public List<User> findActive() throws RepositoryException {
        return findByStatus(DELETED.name());
    }

    @Override
    public List<User> findDeleted() throws RepositoryException {
        return findByStatus(ACTIVE.name());
    }

    @Override
    public void delete(long id) throws RepositoryException {
        repository.setDelete(id,findStatus(DELETED.name()));
    }

    @Override
    public List<User> findByStatus(String statusName) throws RepositoryException {
        return repository.findByStatus(findStatus(statusName));
    }
    private Status findStatus(String name) throws RepositoryException {
        return statusService.findStatus(name);
    }
    public Optional<UserDto> login(AuthenticationDto authenticationDto) {
        return Optional.of(findUserByUsername(authenticationDto.getUsername()));
    }

    @Transactional
    public UserDto oauth(UserDto oauthUser) throws RepositoryException {
        Optional<User> userByUsername = repository.findUserByUsername(oauthUser.getUsername());
        if(userByUsername.isPresent()) {
            update(mapper.toDto(userByUsername.get()));
        }
        else {
            save(oauthUser);
        }
        return findUserByUsername(oauthUser.getUsername());
    }

    @Transactional
    public UserDto findUserByUsername(String username) {
        return mapper.toDto(repository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username - " + username + " not found")));
    }

    public DtoPage<UserDto> findByParamWithDtoPage(UserDto dto) throws RepositoryException {
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(USER + FOUND_BY_PARAM))
                .setContent(mapper.toDtoList(findByParam(dto)))
                .build();
    }

    public DtoPage<UserDto> findTopWithDtoPage() {
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(USER + PAGE))
                .setContent(mapper.toDtoList(findTop()))
                .build();
    }

    private List<User> findTop() {
        return orderRepository.getTop()
                .subList(0, 100);
    }

    private void setDefaultUser(UserDto user) throws RepositoryException {
        if(user.getStatus() == null) {
            Status status = findStatus(ACTIVE.name());
            user.setStatus(statusMapper.toDto(status));
        }
        if(user.getRoles() == null) {
            Role role = roleRepository.findRoleByName(RoleName.USER.name())
                    .orElseThrow(
                            () -> new RepositoryException("Role " + RoleName.USER.name() + " not found")
                    );
             user.setRoles(List.of(roleMapper.toDto(role)));
        }
        user.setPassword(HashGenerator.generateHash(user.getPassword()));
    }
}
