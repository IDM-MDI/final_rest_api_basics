package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.ResponseService;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import com.epam.esm.util.impl.RoleModelMapper;
import com.epam.esm.util.impl.StatusModelMapper;
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

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.service.impl.UserService.USER;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository = Mockito.mock(UserRepository.class);
    @Mock
    private UserModelMapper modelMapper = Mockito.mock(UserModelMapper.class);
    @Mock
    private ResponseService responseService = Mockito.mock(ResponseService.class);
    @Mock
    private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    @InjectMocks
    private UserService service;


    private final User entity;
    private final UserDto dto;
    private final List<User> entityList;
    private final List<UserDto> dtoList;
    private final UserModelMapper mapper;
    private final List<User> top;
    private final ResponseService response;

    UserServiceTest() {
        List<Role> role = List.of(new Role(1L, "user"));
        Status active = new Status(1L,"ACTIVE");

        this.mapper = new UserModelMapper(
                new UserBuilder(),
                new GiftCertificateModelMapper(new TagModelMapper(new TagBuilder()),new GiftCertificateBuilder()),
                new RoleModelMapper(),
                new StatusModelMapper()
        );
        this.response = new ResponseService();


        this.entity = new User(1L,"username","password",null,role,active);
        this.dto = mapper.toDto(entity);
        this.entityList = List.of(
                entity,
                new User(2L,"username2","password2",null,role,active),
                new User(3L,"username3","password3",null,role,active)
                );
        this.dtoList = mapper.toDtoList(entityList);
        this.top = new ArrayList<>();
        for (int i = 0; i < 102; i++) {
            top.add(entity);
        }
    }

    @Test
    void findAllPageWithDtoPage() {
        int page = 1;
        int size = 1;
        String sort = "id";

        ResponseDto responseDto = response.okResponse(USER + PAGE + "page " + page + ", size" + size + ", sort" + sort);
        DtoPage<UserDto> expected = new DtoPage<>(dtoList,responseDto,size,page,sort);

        when(responseService.okResponse(USER + PAGE + "page " + page + ", size" + size + ", sort" + sort))
                .thenReturn(responseDto);
        when(repository.findAll(PageRequest.of(page,size, Sort.by(sort))))
                .thenReturn(new PageImpl<>(entityList));
        when(modelMapper.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<UserDto> actual = service.findAllPageWithDtoPage(page, size, sort);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findByIdWithDtoPage() {
        long id = dto.getId();
        ResponseDto responseDto = response.okResponse(USER + FOUND_BY_ID);
        DtoPage<UserDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        when(responseService.okResponse(USER + FOUND_BY_ID))
                .thenReturn(responseDto);
        when(repository.findById(id))
                .thenReturn(Optional.of(entity));
        when(modelMapper.toDto(entity))
                .thenReturn(dto);

        DtoPage<UserDto> actual = service.findByIdWithDtoPage(id);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void saveWithDtoPage() {
        save();
        ResponseDto responseDto = response.createdResponse(USER + CREATED);
        DtoPage<UserDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        when(responseService.createdResponse(USER + CREATED))
                .thenReturn(responseDto);
        when(modelMapper.toDto(entity)).thenReturn(dto);

        DtoPage<UserDto> actual = service.saveWithDtoPage(dto);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void loginWithDtoPage() {
        login();
        AuthenticationDto auth = new AuthenticationDto(dto.getUsername(),dto.getPassword());
        ResponseDto responseDto = response.okResponse(USER + LOGGED_IN);
        DtoPage<UserDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        when(responseService.okResponse(USER + LOGGED_IN))
                .thenReturn(responseDto);

        DtoPage<UserDto> actual = service.loginWithDtoPage(auth);
        Assertions.assertEquals(expected,actual);
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

    @SneakyThrows
    @Test
    void findByParamWithDtoPage() {
        findByParam();
        ResponseDto responseDto = response.okResponse(USER + FOUND_BY_PARAM);
        DtoPage<UserDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        when(responseService.okResponse(USER + FOUND_BY_PARAM)).thenReturn(responseDto);
        when(modelMapper.toDtoList(List.of(entity))).thenReturn(List.of(dto));

        DtoPage<UserDto> actual = service.findByParamWithDtoPage(dto);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findTopWithDtoPage() {
        ResponseDto responseDto = response.okResponse(USER + PAGE);
        DtoPage<UserDto> expected = new DtoPage<>(dtoList,responseDto,0,0,null);

        when(responseService.okResponse(USER + PAGE)).thenReturn(responseDto);
        when(orderRepository.getTop()).thenReturn(top);
        when(modelMapper.toDtoList(top.subList(0,100))).thenReturn(dtoList);

        DtoPage<UserDto> actual = service.findTopWithDtoPage();
        Assertions.assertEquals(expected,actual);
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
        Assertions.assertThrows(UsernameNotFoundException.class,() -> service.findUserByUsername(dto.getUsername()), "User with username - " + dto.getUsername() + " not found");
    }
}