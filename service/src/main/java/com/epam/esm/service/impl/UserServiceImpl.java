package com.epam.esm.service.impl;

import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleName;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.util.HashGenerator;
import com.epam.esm.util.impl.RoleModelMapper;
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

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_EXCEPTION;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Slf4j
@Profile("prod")
public class UserServiceImpl implements UserService {

    public static final String USER = "User ";
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final UserModelMapper mapper;
    private final RoleModelMapper roleMapper;
    private final OrderRepository orderRepository;
    @Autowired
    public UserServiceImpl(UserRepository repository,
                           RoleRepository roleRepository,
                           UserModelMapper mapper,
                           RoleModelMapper roleMapper,
                           OrderRepository orderRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
        this.roleMapper = roleMapper;
        this.orderRepository = orderRepository;
    }
    @Override
    public User save(UserDto dto) throws RepositoryException {
        setDefaultUser(dto);
        User user = mapper.toEntity(dto);
        User result = repository.save(user);
        log.info("User: " + dto.getUsername() + " successfully added");
        return result;
    }

    @Transactional
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
        if(users.isEmpty()) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_EXCEPTION.toString());
        }
        return users;
    }

    @Override
    public void delete(long id) throws RepositoryException {
        if(!repository.existsById(id)) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }
        repository.setDelete(id,DELETED.name());
    }

    @Override
    public List<User> findByStatus(int page, int size, String sort, String statusName) throws RepositoryException {
        return repository.findByStatus(statusName,PageRequest.of(page, size, Sort.by(sort)));
    }
    public UserDto login(AuthenticationDto authenticationDto) {
        UserDto user = findUserByUsername(authenticationDto.getUsername());
        log.info("User: " + user.getUsername() + " sign in");
        return user;
    }

    @Transactional
    public UserDto oauth(UserDto oauthUser) throws RepositoryException {
        if(repository.existsByUsername(oauthUser.getUsername())) {
            User userByUsername = repository.findUserByUsername(oauthUser.getUsername()).orElse(null);
            update(mapper.toDto(userByUsername));
        }
        else {
            save(oauthUser);
        }
        return findUserByUsername(oauthUser.getUsername());
    }

    public UserDto findUserByUsername(String username) {
        return mapper.toDto(repository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username - " + username + " not found")));
    }


    public List<User> findTop() {
        List<Order> topOrder = orderRepository.getTop(PageRequest.of(0, 100));
        List<User> result = new ArrayList<>();
        topOrder.forEach(i -> result.add(i.getUser()));
        return result;
    }

    private void setDefaultUser(UserDto user) throws RepositoryException {
        if(user.getStatus() == null) {
            user.setStatus(ACTIVE.name());
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
