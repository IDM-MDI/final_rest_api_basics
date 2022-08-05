package com.epam.esm.service.page;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.impl.UserServiceImpl;
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

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.service.impl.UserServiceImpl.USER;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageUserServiceTest {

    @Mock
    private ResponseService responseServiceMock = Mockito.mock(ResponseService.class);
    @Mock
    private UserServiceImpl serviceMock = Mockito.mock(UserServiceImpl.class);
    @Mock
    private UserModelMapper mapperMock = Mockito.mock(UserModelMapper.class);

    @InjectMocks
    private PageUserService pageUserService;

    private final User entity;
    private final UserDto dto;
    private final List<User> entityList;
    private final List<UserDto> dtoList;
    private final UserModelMapper mapper;
    private final List<User> top;
    private final ResponseService responseService;

    public PageUserServiceTest() {
        this.responseService = new ResponseService();
        List<Role> role = List.of(new Role(1L, "user"));

        this.mapper = new UserModelMapper(
                new UserBuilder(),
                new GiftCertificateModelMapper(new TagModelMapper(new TagBuilder()),new GiftCertificateBuilder()),
                new RoleModelMapper()
        );


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


    @Test
    void findByPage() {
        int page = 1;
        int size = 1;
        String sort = "id";

        ResponseDto responseDto = responseService.okResponse(USER + PAGE + "page " + page + ", size" + size + ", sort" + sort);
        DtoPage<UserDto> expected = new DtoPage<>(dtoList,responseDto,size,page,sort);

        when(responseServiceMock.okResponse(USER + PAGE + "page " + page + ", size" + size + ", sort" + sort))
                .thenReturn(responseDto);
        when(serviceMock.findAll(page,size,sort))
                .thenReturn(entityList);
        when(mapperMock.toDtoList(entityList))
                .thenReturn(dtoList);

        DtoPage<UserDto> actual = pageUserService.findByPage(page, size, sort);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findById() {
        long id = dto.getId();
        ResponseDto responseDto = responseService.okResponse(USER + FOUND_BY_ID);
        DtoPage<UserDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        when(responseServiceMock.okResponse(USER + FOUND_BY_ID))
                .thenReturn(responseDto);
        when(serviceMock.findById(id)).thenReturn(entity);
        when(mapperMock.toDto(entity))
                .thenReturn(dto);

        DtoPage<UserDto> actual = pageUserService.findById(id);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void save() {
        ResponseDto responseDto = responseService.createdResponse(USER + CREATED);
        DtoPage<UserDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        when(responseServiceMock.createdResponse(USER + CREATED))
                .thenReturn(responseDto);
        when(serviceMock.save(dto)).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(dto);

        DtoPage<UserDto> actual = pageUserService.save(dto);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void update() {
        ResponseDto responseDto = responseService.createdResponse(USER + UPDATED);
        DtoPage<UserDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        when(responseServiceMock.createdResponse(USER + UPDATED))
                .thenReturn(responseDto);
        when(serviceMock.update(dto)).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(dto);

        DtoPage<UserDto> actual = pageUserService.update(dto,dto.getId());
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void delete() {
        long id = dto.getId();
        ResponseDto responseDto = responseService.okResponse(USER + DELETED);
        DtoPage<UserDto> expected = new DtoPage<>(null,responseDto,0,0,null);

        when(responseServiceMock.okResponse(USER + DELETED))
                .thenReturn(responseDto);
        doNothing().when(serviceMock).delete(id);

        DtoPage<UserDto> actual = pageUserService.delete(id);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void login() {
        AuthenticationDto auth = new AuthenticationDto(dto.getUsername(),dto.getPassword());
        ResponseDto responseDto = responseService.okResponse(USER + LOGGED_IN);
        DtoPage<UserDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        when(serviceMock.login(auth)).thenReturn(dto);
        when(responseServiceMock.okResponse(USER + LOGGED_IN))
                .thenReturn(responseDto);

        DtoPage<UserDto> actual = pageUserService.login(auth);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findByParam() {
        ResponseDto responseDto = responseService.okResponse(USER + FOUND_BY_PARAM);
        DtoPage<UserDto> expected = new DtoPage<>(List.of(dto),responseDto,0,0,null);

        when(responseServiceMock.okResponse(USER + FOUND_BY_PARAM)).thenReturn(responseDto);
        when(serviceMock.findByParam(dto)).thenReturn(List.of(entity));
        when(mapperMock.toDtoList(List.of(entity))).thenReturn(List.of(dto));

        DtoPage<UserDto> actual = pageUserService.findByParam(dto);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findByActiveStatus() {
    }

    @Test
    void findByStatus() {
    }

    @Test
    void findTop() {
        ResponseDto responseDto = responseService.okResponse(USER + PAGE);
        DtoPage<UserDto> expected = new DtoPage<>(dtoList,responseDto,0,0,null);

        when(responseServiceMock.okResponse(USER + PAGE)).thenReturn(responseDto);
        when(serviceMock.findTop()).thenReturn(entityList);
        when(mapperMock.toDtoList(entityList)).thenReturn(dtoList);

        DtoPage<UserDto> actual = pageUserService.findTop();
        Assertions.assertEquals(expected,actual);
    }
}