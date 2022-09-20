package com.epam.esm.service.page;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.*;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.impl.OrderServiceImpl;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.service.impl.OrderServiceImpl.ORDER;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageOrderServiceTest {
    private static final int PAGE = 0;
    private static final int SIZE = 0;
    private static final String SORT = "ID";
    private static final String DIRECTION = "asc";
    @Mock
    private OrderServiceImpl serviceMock = Mockito.mock(OrderServiceImpl.class);
    @Mock
    private UserServiceImpl userServiceImplMock = Mockito.mock(UserServiceImpl.class);
    @Mock
    private GiftCertificateModelMapper giftMapperMock = Mockito.mock(GiftCertificateModelMapper.class);
    @Mock
    private ResponseService responseServiceMock = Mockito.mock(ResponseService.class);
    @InjectMocks
    private PageOrderService pageOrderService;

    private final OrderDto dto;
    private final Order entity;

    private final User userEntity;
    private final UserDto userDto;
    private final UserModelMapper userMapper;
    private final GiftCertificateModelMapper giftMapper;
    private final GiftCertificate giftEntity;
    private final GiftCertificateDto giftDto;
    private final ResponseService responseService;

    public PageOrderServiceTest() {
        List<Role> role = List.of(new Role(1L, "user"));
        this.responseService = new ResponseService();
        this.giftMapper = new GiftCertificateModelMapper(new TagModelMapper(new TagBuilder()),new GiftCertificateBuilder());
        this.userMapper = new UserModelMapper(new UserBuilder(),giftMapper,new RoleModelMapper());

        this.userEntity = new User(1L,"username","password",null,role,ACTIVE.name());
        this.userDto = userMapper.toDto(userEntity);

        this.giftEntity = new GiftCertificate(
                1L,
                "test",
                "test",
                new BigDecimal("0"),
                1,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                ACTIVE.name()
        );
        giftDto = giftMapper.toDto(giftEntity);
        dto = new OrderDto(
                1L,
                giftDto.getPrice(),
                null,
                giftDto,
                giftDto.getId(),
                userDto.getId(),
                ACTIVE.name()
        );
        entity = new Order(
                1L,
                giftDto.getPrice(),
                null,
                giftEntity,
                userEntity,
                ACTIVE.name()
        );
    }

    @SneakyThrows
    @Test
    void save() {
        OrderDto orderDto = new OrderDto(0,null,null,null,giftDto.getId(),userDto.getId(),null);
        ResponseDto responseDto = responseService.createdResponse(ORDER + CREATED);
        DtoPage<OrderDto> expected = new DtoPage<>(
                List.of(dto),responseDto,0,0,null,null,false,null,null
        );
        when(userServiceImplMock.findUserDtoByUsername(userDto.getUsername())).thenReturn(userDto);
        when(responseServiceMock.createdResponse(ORDER + CREATED)).thenReturn(responseDto);
        when(serviceMock.save(orderDto)).thenReturn(entity);
        when(giftMapperMock.toDto(entity.getGift())).thenReturn(giftDto);

        DtoPage<OrderDto> actual = pageOrderService.save(userDto.getUsername(),giftDto.getId());
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void SaveWithOrderDto() {
        ResponseDto responseDto = responseService.createdResponse(ORDER + CREATED);
        DtoPage<OrderDto> expected = new DtoPage<>(
                List.of(dto),responseDto,0,0,null,null,false,null,null
        );

        when(responseServiceMock.createdResponse(ORDER + CREATED)).thenReturn(responseDto);
        when(serviceMock.save(dto)).thenReturn(entity);
        when(giftMapperMock.toDto(entity.getGift())).thenReturn(giftDto);

        DtoPage<OrderDto> actual = pageOrderService.save(dto);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void update() {
        ResponseDto responseDto = responseService.createdResponse(ORDER + UPDATED);
        DtoPage<OrderDto> expected = new DtoPage<>(
                List.of(dto),responseDto,0,0,null,null,false,null,null
        );

        when(responseServiceMock.createdResponse(ORDER + UPDATED)).thenReturn(responseDto);
        when(serviceMock.update(dto)).thenReturn(entity);
        when(giftMapperMock.toDto(entity.getGift())).thenReturn(giftDto);

        DtoPage<OrderDto> actual = pageOrderService.update(dto,dto.getId());
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void delete() {
        ResponseDto responseDto = responseService.okResponse(ORDER + DELETED);
        DtoPage<OrderDto> expected = new DtoPage<>(
                null,responseDto,0,0,null,null,false,null,null
        );

        when(responseServiceMock.okResponse(ORDER + DELETED)).thenReturn(responseDto);
        doNothing().when(serviceMock).delete(dto.getId());

        DtoPage<OrderDto> actual = pageOrderService.delete(dto.getId());
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findByPage() {
        ResponseDto responseDto = responseService.okResponse(pageResponseTemplate(ORDER,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<OrderDto> expected = new DtoPage<>(
                List.of(dto),responseDto,SIZE,PAGE,SORT,DIRECTION,false,null,null
        );

        when(responseServiceMock.okResponse(pageResponseTemplate(ORDER,PAGE,SIZE,SORT,DIRECTION))).thenReturn(responseDto);
        when(serviceMock.findAll(PAGE,SIZE,SORT,DIRECTION)).thenReturn(List.of(entity));
        when(serviceMock.findAll(PAGE + 1,SIZE,SORT,DIRECTION)).thenReturn(new ArrayList<>());
        when(giftMapperMock.toDto(entity.getGift())).thenReturn(giftDto);

        DtoPage<OrderDto> actual = pageOrderService.findByPage(PAGE,SIZE,SORT,DIRECTION);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void findById() {
        ResponseDto responseDto = responseService.okResponse(ORDER + FOUND_BY_ID);
        DtoPage<OrderDto> expected = new DtoPage<>(
                List.of(dto),responseDto,0,0,null,null,false,null,null
        );

        when(responseServiceMock.okResponse(ORDER + FOUND_BY_ID)).thenReturn(responseDto);
        when(serviceMock.findById(dto.getId())).thenReturn(entity);
        when(giftMapperMock.toDto(entity.getGift())).thenReturn(giftDto);

        DtoPage<OrderDto> actual = pageOrderService.findById(dto.getId());
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findByParam() {
        ResponseDto responseDto = responseService.okResponse(ORDER + FOUND_BY_PARAM);
        DtoPage<OrderDto> expected = new DtoPage<>(
                List.of(dto),responseDto,0,0,null,null,false,null,null
        );

        when(responseServiceMock.okResponse(ORDER + FOUND_BY_PARAM)).thenReturn(responseDto);
        when(serviceMock.findByParam(dto)).thenReturn(List.of(entity));
        when(giftMapperMock.toDto(entity.getGift())).thenReturn(giftDto);

        DtoPage<OrderDto> actual = pageOrderService.findByParam(dto);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findByActiveStatus() {
        ResponseDto responseDto = responseService.okResponse(pageResponseTemplate(ORDER,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<OrderDto> expected = new DtoPage<>(
                List.of(dto),responseDto,SIZE,PAGE,SORT,DIRECTION,false,null,null
        );

        when(responseServiceMock.okResponse(pageResponseTemplate(ORDER,PAGE,SIZE,SORT,DIRECTION)))
                .thenReturn(responseDto);
        when(serviceMock.findByStatus(PAGE,SIZE,SORT,DIRECTION,ACTIVE.name())).thenReturn(List.of(entity));
        when(giftMapperMock.toDto(entity.getGift())).thenReturn(giftDto);

        DtoPage<OrderDto> actual = pageOrderService.findByActiveStatus(PAGE,SIZE,SORT,DIRECTION);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findUsersByStatus() {
        ResponseDto responseDto = responseService.okResponse(pageResponseTemplate(ORDER,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<OrderDto> expected = new DtoPage<>(
                List.of(dto),responseDto,SIZE,PAGE,SORT,DIRECTION,false,null,null
        );

        when(responseServiceMock.okResponse(pageResponseTemplate(ORDER,PAGE,SIZE,SORT,DIRECTION))).thenReturn(responseDto);
        when(serviceMock.findByStatus(PAGE,SIZE,SORT,DIRECTION,ACTIVE.name())).thenReturn(List.of(entity));
        when(giftMapperMock.toDto(entity.getGift())).thenReturn(giftDto);

        DtoPage<OrderDto> actual = pageOrderService.findByStatus(PAGE,SIZE,SORT,DIRECTION,ACTIVE.name());
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void updateWithUsername() {
        String username = userEntity.getUsername();

        ResponseDto responseDto = responseService.createdResponse(ORDER + UPDATED);
        DtoPage<OrderDto> expected = new DtoPage<>(
                List.of(dto),responseDto,0,0,null,null,false,null,null
        );

        when(responseServiceMock.createdResponse(ORDER + UPDATED)).thenReturn(responseDto);
        when(serviceMock.update(dto,username)).thenReturn(entity);
        when(giftMapperMock.toDto(entity.getGift())).thenReturn(giftDto);

        DtoPage<OrderDto> actual = pageOrderService.update(username,dto,dto.getId());
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void deleteWithUsername() {
        String username = userEntity.getUsername();
        ResponseDto responseDto = responseService.okResponse(ORDER + DELETED);
        DtoPage<OrderDto> expected = new DtoPage<>(
                null,responseDto,0,0,null,null,false,null,null
        );

        when(responseServiceMock.okResponse(ORDER + DELETED)).thenReturn(responseDto);
        doNothing().when(serviceMock).delete(dto.getId(),username);

        DtoPage<OrderDto> actual = pageOrderService.delete(username,dto.getId());
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findByPageWithUsername() {
        String username = userEntity.getUsername();
        ResponseDto responseDto = responseService.okResponse(pageResponseTemplate(ORDER,PAGE,SIZE,SORT,DIRECTION));
        DtoPage<OrderDto> expected = new DtoPage<>(
                List.of(dto),responseDto,SIZE,PAGE,SORT,DIRECTION,false,null,ControllerType.ORDER_USER
        );

        when(responseServiceMock.okResponse(pageResponseTemplate(ORDER,PAGE,SIZE,SORT,DIRECTION))).thenReturn(responseDto);
        when(serviceMock.findAll(PAGE,SIZE,SORT,DIRECTION, username)).thenReturn(List.of(entity));
        when(serviceMock.findAll(PAGE + 1,SIZE,SORT,DIRECTION, username)).thenReturn(new ArrayList<>());
        when(giftMapperMock.toDto(entity.getGift())).thenReturn(giftDto);

        DtoPage<OrderDto> actual = pageOrderService.findByPage(PAGE,SIZE,SORT,DIRECTION, username);
        Assertions.assertEquals(expected,actual);
    }
}