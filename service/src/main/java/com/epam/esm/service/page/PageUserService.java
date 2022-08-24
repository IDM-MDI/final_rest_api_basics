package com.epam.esm.service.page;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.ControllerType;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.service.PageService;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.util.impl.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.service.impl.UserServiceImpl.USER;

@Service
public class PageUserService implements PageService<DtoPage<UserDto>,UserDto> {
    private final UserServiceImpl service;
    private final ResponseService responseService;
    private final UserModelMapper mapper;

    @Autowired
    public PageUserService(UserServiceImpl service, ResponseService responseService, UserModelMapper mapper) {
        this.service = service;
        this.responseService = responseService;
        this.mapper = mapper;
    }

    public DtoPage<UserDto> findByPage(int page, int size, String sort, String direction) {
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(
                        USER + PAGE + "page " + page + ", size" + size + ", sort" + sort
                ))
                .setContent(mapper.toDtoList(service.findAll(page,size,sort,direction)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .setDirection(direction)
                .setHasNext(!service.findAll(page + 1,size,sort,direction).isEmpty())
                .setType(ControllerType.USER_ALL)
                .build();
    }

    public DtoPage<UserDto> findById(long id) throws RepositoryException {
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(USER + FOUND_BY_ID))
                .setContent(List.of(mapper.toDto(service.findById(id))))
                .build();
    }

    public DtoPage<UserDto> save(UserDto dto) throws RepositoryException {
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.createdResponse(USER + CREATED))
                .setContent(List.of(mapper.toDto(service.save(dto))))
                .build();
    }

    @Override
    public DtoPage<UserDto> update(UserDto dto, long id) throws RepositoryException {
        dto.setId(id);
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.createdResponse(USER + UPDATED))
                .setContent(List.of(mapper.toDto(service.update(dto))))
                .build();
    }

    @Override
    public DtoPage<UserDto> delete(long id) throws RepositoryException {
        service.delete(id);
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(USER + DELETED))
                .build();
    }

    public DtoPage<UserDto> login(AuthenticationDto dto) {
        UserDto user = service.login(dto);
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(USER + LOGGED_IN))
                .setContent(List.of(user))
                .build();
    }

    public DtoPage<UserDto> findByParam(UserDto dto) throws RepositoryException {
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(USER + FOUND_BY_PARAM))
                .setContent(mapper.toDtoList(service.findByParam(dto)))
                .build();
    }

    @Override
    public DtoPage<UserDto> findByActiveStatus(int page, int size, String sort, String direction) throws RepositoryException {
        return findByStatus(page,size,sort,direction,ACTIVE.name());
    }

    @Override
    public DtoPage<UserDto> findByStatus(int page, int size, String sort, String direction, String statusName) throws RepositoryException {
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(
                        USER + PAGE + "page " + page + ", size" + size + ", sort" + sort
                ))
                .setContent(mapper.toDtoList(service.findByStatus(page,size,sort,direction,statusName)))
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .setDirection(direction)
                .setHasNext(!service.findByStatus(page + 1,size,sort,direction,statusName).isEmpty())
                .setType(ControllerType.USER_ALL)
                .build();
    }

    public DtoPage<UserDto> findTop() {
        return new DtoPageBuilder<UserDto>()
                .setResponse(responseService.okResponse(USER + PAGE))
                .setContent(mapper.toDtoList(service.findTop()))
                .build();
    }
}
