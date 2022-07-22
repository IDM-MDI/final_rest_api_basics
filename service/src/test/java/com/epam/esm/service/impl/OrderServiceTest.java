package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.*;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.ResponseService;
import com.epam.esm.util.impl.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository = Mockito.mock(OrderRepository.class);
    @Mock
    private GiftCertificateRepository giftRepository = Mockito.mock(GiftCertificateRepository.class);
    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    @Mock
    private UserService userService = Mockito.mock(UserService.class);
    @Mock
    private GiftCertificateModelMapper mapper = Mockito.mock(GiftCertificateModelMapper.class);

    @InjectMocks
    private OrderService service;

    private final OrderDto dto;
    private final Order entity;

    private final User userEntity;
    private final UserDto userDto;
    private final UserModelMapper userMapper;
    private final GiftCertificateModelMapper giftMapper;
    private final ResponseService response;
    private final GiftCertificate giftEntity;
    private final GiftCertificateDto giftDto;

    OrderServiceTest() {
        List<Role> role = List.of(new Role(1L, "user"));
        Status active = new Status(1L,"ACTIVE");
        this.giftMapper = new GiftCertificateModelMapper(new TagModelMapper(new TagBuilder()),new GiftCertificateBuilder());
        this.userMapper = new UserModelMapper(new UserBuilder(),giftMapper,new RoleModelMapper(),new StatusModelMapper());
        this.response = new ResponseService();


        this.userEntity = new User(1L,"username","password",null,role,active);
        this.userDto = userMapper.toDto(userEntity);

        this.giftEntity = new GiftCertificate(
                1L,
                "test",
                "test",
                new BigDecimal("0"),
                1,
                null,null,null,active
        );
        giftDto = giftMapper.toDto(giftEntity);
        dto = new OrderDto(
                1L,
                giftDto.getPrice(),
                null,
                giftDto,
                giftDto.getId(),
                userDto.getId()
        );
        entity = new Order(
                1L,
                giftDto.getPrice(),
                null,
                giftEntity,
                userEntity,
                null
        );
    }

    @SneakyThrows
    @Test
    void saveByUserWithDtoPage() {
        save();

        DtoPage<OrderDto> expected = new DtoPage<>(List.of(dto),null,0,0,null);

        when(userService.findUserByUsername(userDto.getUsername())).thenReturn(userDto);
        when(mapper.toDto(giftEntity)).thenReturn(giftDto);

        DtoPage<OrderDto> actual = service.saveByUserWithDtoPage(userDto.getUsername(), giftDto.getId());
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void save() {
        when(userRepository.findById(dto.getUserId()))
                .thenReturn(Optional.of(userEntity));
        when(giftRepository.findById(dto.getGiftId()))
                .thenReturn(Optional.of(giftEntity));
        when(repository.save(any())).thenReturn(entity);

        Order actual = service.save(dto);
        Assertions.assertEquals(entity,actual);
    }
}