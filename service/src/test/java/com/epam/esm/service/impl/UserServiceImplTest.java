package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.RoleRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.RoleName.USER;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository = Mockito.mock(UserRepository.class);
    @Mock
    private UserModelMapper modelMapper = Mockito.mock(UserModelMapper.class);
    @Mock
    private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    @Mock
    private RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    @Mock
    private RoleModelMapper roleMapper = Mockito.mock(RoleModelMapper.class);

    @InjectMocks
    private UserServiceImpl service;


    private final User entity;
    private final UserDto dto;
    private final List<User> entityList;
    private final List<UserDto> dtoList;
    private final UserModelMapper mapper;
    private final ResponseService response;

    UserServiceImplTest() {
        this.mapper = new UserModelMapper(
                new UserBuilder(),
                new GiftCertificateModelMapper(new TagModelMapper(new TagBuilder()),new GiftCertificateBuilder()),
                new RoleModelMapper()
        );
        this.response = new ResponseService();


        this.entity = new User(1L,"username","password",null,null,ACTIVE.name());
        this.dto = mapper.toDto(entity);
        this.entityList = List.of(
                entity,
                new User(2L,"username2","password2",null,null,ACTIVE.name()),
                new User(3L,"username3","password3",null,null,ACTIVE.name())
                );
        this.dtoList = mapper.toDtoList(entityList);
    }

    @SneakyThrows
    @Test
    void save() {
        Role roleEntity = new Role(1L, "USER");
        RoleDto roleDto = new RoleDto(1L, "USER");

        when(roleRepository.findRoleByName(USER.name())).thenReturn(Optional.of(roleEntity));
        when(roleMapper.toDto(roleEntity)).thenReturn(roleDto);
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        User actual = service.save(dto);
        Assertions.assertEquals(entity,actual);
    }

    @SneakyThrows
    @Test
    void saveShouldThrowRepositoryException() {
        UserDto user = new UserDto();
        when(roleRepository.findRoleByName(USER.name())).thenReturn(Optional.empty());
        Assertions.assertThrows(RepositoryException.class,()-> service.save(user));
    }

    @Test
    void update() {
        when(repository.findUserByUsername(dto.getUsername()))
                .thenReturn(Optional.of(entity));
        when(modelMapper.toDto(entity))
                .thenReturn(dto);
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

    @SneakyThrows
    @Test
    void findByParamShouldThrowRepositoryException() {
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.findAll(Example.of(entity))).thenReturn(List.of());
        Assertions.assertThrows(RepositoryException.class,()-> service.findByParam(dto));
    }

    @Test
    void login() {
        AuthenticationDto auth = new AuthenticationDto(dto.getUsername(),dto.getPassword());
        when(repository.findUserByUsername(auth.getUsername()))
                .thenReturn(Optional.of(entity));
        when(modelMapper.toDto(entity))
                .thenReturn(dto);
        UserDto actual = service.login(auth);
        Assertions.assertEquals(dto,actual);
    }

    @SneakyThrows
    @Test
    void oauthUpdate() {
        when(repository.existsByUsername(dto.getUsername())).thenReturn(true);
        when(repository.findUserByUsername(dto.getUsername()))
                .thenReturn(Optional.of(entity));
        when(modelMapper.toDto(entity))
                .thenReturn(dto);
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        UserDto actual = service.oauth(dto);
        Assertions.assertEquals(dto,actual);
    }

    @SneakyThrows
    @Test
    void oauthSave() {
        Role roleEntity = new Role(1L, "USER");
        RoleDto roleDto = new RoleDto(1L, "USER");
        when(repository.existsByUsername(dto.getUsername())).thenReturn(false);
        when(modelMapper.toDto(entity))
                .thenReturn(dto);
        when(modelMapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(roleRepository.findRoleByName(USER.name())).thenReturn(Optional.of(roleEntity));
        when(roleMapper.toDto(roleEntity)).thenReturn(roleDto);
        when(repository.findUserByUsername(dto.getUsername()))
                .thenReturn(Optional.of(entity));
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

    @SneakyThrows
    @Test
    void findById() {
        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        User actual = service.findById(entity.getId());
        Assertions.assertEquals(entity,actual);
    }

    @SneakyThrows
    @Test
    void findByIdShouldThrowRepositoryException() {
        when(repository.findById(entity.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(RepositoryException.class,()-> service.findById(entity.getId()));
    }

    @SneakyThrows
    @Test
    void delete() {
        when(repository.existsById(entity.getId())).thenReturn(true);
        doNothing().when(repository).setDelete(entity.getId(),DELETED.name());
        service.delete(entity.getId());
        verify(repository).existsById(entity.getId());
    }

    @SneakyThrows
    @Test
    void deleteShouldThrowRepositoryException() {
        when(repository.existsById(entity.getId())).thenReturn(false);
        Assertions.assertThrows(RepositoryException.class,()->service.delete(entity.getId()));
    }

    @Test
    void findAll() {
        int page = 0;
        int size = 1;
        String sort = "id";
        when(repository.findAll(PageRequest.of(page,size, Sort.by(sort)))).thenReturn(new PageImpl<>(entityList));
        List<User> actual = service.findAll(page, size, sort);
        Assertions.assertEquals(entityList,actual);
    }

    @Test
    void findTop() {
        List<User> expected = List.of(entity, entity, entity);
        int page = 0;
        int size = 100;
        when(orderRepository.getTop(PageRequest.of(page, size)))
                .thenReturn(
                        List.of(
                                new Order(null,null,null,null,entity,null),
                                new Order(null,null,null,null,entity,null),
                                new Order(null,null,null,null,entity,null)
                        )
                );

        List<User> actual = service.findTop();

        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findByStatus() {
        int page = 0;
        int size = 1;
        String sort = "id";
        String statusName = ACTIVE.name();

        when(repository.findByStatus(statusName,PageRequest.of(page,size, Sort.by(sort)))).thenReturn(entityList);
        List<User> actual = service.findByStatus(page, size, sort,statusName);
        Assertions.assertEquals(entityList,actual);
    }
}