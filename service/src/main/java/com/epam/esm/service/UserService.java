package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.impl.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class UserService {

    private final UserRepository repository;
    private final UserModelMapper mapper;

    @Autowired
    public UserService(UserRepository repository, UserModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void save(UserDto dto) {
        repository.save(mapper.toEntity(dto));
    }

    public Long delete(Long id) {
        return repository.setDelete(id);
    }

    public User update(UserDto dto, Long id) {
        User user = mapper.toEntity(dto);
        user.setId(id);
        return repository.save(user);
    }

    public List<UserDto> findAll() {
        return mapper.toDtoList((List<User>) repository.findAll());
    }

    public UserDto findById(Long id) {
        return mapper.toDto(repository.findById(id).get());
    }
}
