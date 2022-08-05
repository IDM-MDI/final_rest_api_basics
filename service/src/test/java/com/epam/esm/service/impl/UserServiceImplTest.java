package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.ResponseService;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import com.epam.esm.util.impl.RoleModelMapper;
import com.epam.esm.util.impl.TagModelMapper;
import com.epam.esm.util.impl.UserModelMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository = Mockito.mock(UserRepository.class);
    @Mock
    private UserModelMapper modelMapper = Mockito.mock(UserModelMapper.class);
    @Mock
    private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    @InjectMocks
    private UserServiceImpl service;


    private final User entity;
    private final UserDto dto;
    private final List<User> entityList;
    private final List<UserDto> dtoList;
    private final UserModelMapper mapper;
    private final List<User> top;
    private final ResponseService response;

    UserServiceImplTest() {
        List<Role> role = List.of(new Role(1L, "user"));

        this.mapper = new UserModelMapper(
                new UserBuilder(),
                new GiftCertificateModelMapper(new TagModelMapper(new TagBuilder()),new GiftCertificateBuilder()),
                new RoleModelMapper()
        );
        this.response = new ResponseService();


        this.entity = new User(1L,"username","password",null,role,ACTIVE.name());
        this.dto = mapper.toDto(entity);
        this.entityList = List.of(
                entity,
                new User(2L,"username2","password2",null,role,ACTIVE.name()),
                new User(3L,"username3","password3",null,role,ACTIVE.name())
                );
        this.dtoList = mapper.toDtoList(entityList);
        this.top = new ArrayList<>();
        for (int i = 0; i < 102; i++) {
            top.add(entity);
        }
    }

    @SneakyThrows
    @Test
    void save() {
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        User actual = service.save(dto);
        Assertions.assertEquals(entity,actual);
    }

    @Test
    void update() {
        findUserByUsername();
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        User actual = service.update(dto);
        Assertions.assertEquals(entity,actual);
    }

    @SneakyThrows
    @Test
    void findByParam() {
        List<User> expected = List.of(entity);
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.findAll(Example.of(entity))).thenReturn(List.of(entity));

        List<User> actual = service.findByParam(dto);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void login() {
        findUserByUsername();
        AuthenticationDto auth = new AuthenticationDto(dto.getUsername(),dto.getPassword());
        UserDto actual = service.login(auth);
        Assertions.assertEquals(dto,actual);
    }

    @SneakyThrows
    @Test
    void oauth() {
        findUserByUsername();
        update();
        save();
        UserDto actual = service.oauth(dto);
        Assertions.assertEquals(dto,actual);
    }

    @Test
    void findUserByUsername() {
        AuthenticationDto auth = new AuthenticationDto(dto.getUsername(),dto.getPassword());
        when(repository.findUserByUsername(auth.getUsername()))
                .thenReturn(Optional.of(entity));
        when(modelMapper.toDto(entity))
                .thenReturn(dto);

        UserDto actual = service.findUserByUsername(auth.getUsername());

        Assertions.assertEquals(dto,actual);
    }
    @Test
    void findUserByUsernameShouldThrowException() {
        Assertions.assertThrows(UsernameNotFoundException.class,() -> {
            service.findUserByUsername(dto.getUsername());
        });
    }
}