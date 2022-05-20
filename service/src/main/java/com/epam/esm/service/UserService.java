package com.epam.esm.service;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.impl.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public DtoPage<UserDto> findAll(Integer page, Integer size, String sort) {
        List<User> userList = repository.findAll(PageRequest.of(page,size, Sort.by(sort))).toList();
        DtoPage<UserDto> dtoPage = new DtoPage<>();
        dtoPage.setContent(mapper.toDtoList(userList));
        dtoPage.setNumberOfPage(page);
        dtoPage.setSize(size);
        dtoPage.setSortBy(sort);
        return dtoPage;
    }

    public UserDto findById(Long id) {
        return mapper.toDto(repository.findById(id).get());
    }
}
