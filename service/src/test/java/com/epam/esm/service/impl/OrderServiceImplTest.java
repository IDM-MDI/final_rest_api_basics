package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.OrderBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;
import static com.epam.esm.exception.ServiceExceptionCode.SERVICE_BAD_STATUS;
import static com.epam.esm.validator.SortValidator.getValidSort;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository repository = Mockito.mock(OrderRepository.class);
    @Mock
    private GiftCertificateRepository giftRepository = Mockito.mock(GiftCertificateRepository.class);
    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    @Mock
    private UserServiceImpl userService = Mockito.mock(UserServiceImpl.class);
    @Mock
    private GiftCertificateServiceImpl giftService = Mockito.mock(GiftCertificateServiceImpl.class);
    @Mock
    private GiftCertificateModelMapper mapper = Mockito.mock(GiftCertificateModelMapper.class);

    @InjectMocks
    private OrderServiceImpl service;

    private final OrderDto dto;
    private final Order entity;

    private final User userEntity;
    private final UserDto userDto;
    private final UserModelMapper userMapper;
    private final GiftCertificateModelMapper giftMapper;
    private final GiftCertificate giftEntity;
    private final GiftCertificateDto giftDto;

    OrderServiceImplTest() {
        List<Role> role = List.of(new Role(1L, "user"));
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
        when(userService.findById(dto.getUserId()))
                .thenReturn(userEntity);
        when(giftService.findById(dto.getGiftId()))
                .thenReturn(giftEntity);
        when(repository.save(any())).thenReturn(entity);

        Order actual = service.save(dto);
        Assertions.assertEquals(entity,actual);
    }

    @SneakyThrows
    @Test
    void update() {
        Order expected = new OrderBuilder()
                .setId(entity.getId())
                .setGift(entity.getGift())
                .setUser(entity.getUser())
                .setPrice(entity.getPrice())
                .setStatus(DELETED.name())
                .setPurchaseTime(entity.getPurchaseTime()).build();
        OrderDto updatedDto = new OrderDto(
                expected.getId(),
                expected.getPrice(),
                expected.getPurchaseTime(),
                giftMapper.toDto(expected.getGift()),
                expected.getGift().getId(),
                expected.getUser().getId(),
                expected.getStatus()
        );
        long id = expected.getId();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(expected);
        Order actual = service.update(updatedDto);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void updateShouldThrowServiceException() {
        Order expected = new OrderBuilder()
                .setId(entity.getId())
                .setGift(entity.getGift())
                .setUser(entity.getUser())
                .setPrice(entity.getPrice())
                .setStatus(DELETED.name())
                .setPurchaseTime(entity.getPurchaseTime()).build();
        OrderDto updatedDto = new OrderDto(
                expected.getId(),
                expected.getPrice(),
                expected.getPurchaseTime(),
                giftMapper.toDto(expected.getGift()),
                expected.getGift().getId(),
                expected.getUser().getId(),
                null
        );
        long id = expected.getId();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        Assertions.assertThrows(ServiceException.class,() -> service.update(updatedDto), SERVICE_BAD_STATUS.toString());
    }

    @Test
    void findAll() {
        int page = 0;
        int size = 1;
        String sort = "id";
        String direction = "asc";
        when(repository.findAll(PageRequest.of(page,size,getValidSort(sort,direction))))
                .thenReturn(new PageImpl<>(List.of(entity)));
        List<Order> actual = service.findAll(page, size, sort, direction);
        Assertions.assertEquals(actual.size(), size);
    }

    @SneakyThrows
    @Test
    void findById() {
        long id = entity.getId();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        Order actual = service.findById(id);
        Assertions.assertEquals(entity,actual);
    }

    @SneakyThrows
    @Test
    void findByIdShouldThrowRepositoryException() {
        long id = entity.getId();
        when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(RepositoryException.class,() -> service.findById(id),REPOSITORY_NOTHING_FIND_BY_ID.toString());
    }

    @Test
    void findByParam() {
        Assertions.assertTrue(service.findByParam(new OrderDto()).isEmpty());
    }

    @SneakyThrows
    @Test
    void delete() {
        long id = 1;
        doNothing().when(repository).setDelete(id,DELETED.name());
        service.delete(id);
        verify(repository).setDelete(id,DELETED.name());
    }

    @Test
    void findByStatus() {
        Assertions.assertTrue(service.findByStatus(1,1,"id","asc",DELETED.name()).isEmpty());
    }

    @Test
    void getCount() {
        long expected = 1;
        when(repository.count()).thenReturn(expected);
        long actual = service.getCount();
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void updateWithUsername() {
        Order expected = new OrderBuilder()
                .setId(entity.getId())
                .setGift(entity.getGift())
                .setUser(entity.getUser())
                .setPrice(entity.getPrice())
                .setStatus(DELETED.name())
                .setPurchaseTime(entity.getPurchaseTime()).build();
        OrderDto updatedDto = new OrderDto(
                expected.getId(),
                expected.getPrice(),
                expected.getPurchaseTime(),
                giftMapper.toDto(expected.getGift()),
                expected.getGift().getId(),
                expected.getUser().getId(),
                expected.getStatus()
        );
        long id = expected.getId();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(expected);
        when(userService.findUserByUsername(userEntity.getUsername()))
                .thenReturn(userEntity);
        Order actual = service.update(updatedDto,userEntity.getUsername());
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void updateWithUsernameShouldThrowServiceException() {
        Order expected = new OrderBuilder()
                .setId(entity.getId())
                .setGift(entity.getGift())
                .setUser(entity.getUser())
                .setPrice(entity.getPrice())
                .setStatus(DELETED.name())
                .setPurchaseTime(entity.getPurchaseTime()).build();
        OrderDto updatedDto = new OrderDto(
                expected.getId(),
                expected.getPrice(),
                expected.getPurchaseTime(),
                giftMapper.toDto(expected.getGift()),
                expected.getGift().getId(),
                expected.getUser().getId() + 1,
                expected.getStatus()
        );
        long id = expected.getId();
        when(userService.findUserByUsername(userEntity.getUsername()))
                .thenReturn(userEntity);
        Assertions.assertThrows(ServiceException.class, () -> service.update(updatedDto,userEntity.getUsername()));
    }

    @SneakyThrows
    @Test
    void deleteWithUsername() {
        long id = 1;
        when(userService.findUserByUsername(userEntity.getUsername()))
                .thenReturn(userEntity);
        when(repository.findById(id))
                .thenReturn(Optional.of(entity));
        doNothing().when(repository)
                .setDelete(id,DELETED.name());
        service.delete(id,userEntity.getUsername());
        verify(repository).setDelete(id,DELETED.name());
    }

    @SneakyThrows
    @Test
    void deleteWithUsernameShouldThrowServiceException() {
        long id = 1;
        when(userService.findUserByUsername(userEntity.getUsername()))
                .thenReturn(new User());
        when(repository.findById(id))
                .thenReturn(Optional.of(entity));
        Assertions.assertThrows(ServiceException.class, () -> service.delete(id,userEntity.getUsername()));
    }

    @Test
    void findAllWithUsername() {
        int page = 0;
        int size = 1;
        String sort = "id";
        String direction = "asc";
        List<Order> expected = List.of(entity);

        when(userService.findUserByUsername(userEntity.getUsername()))
                .thenReturn(userEntity);
        when(repository.findOrdersByUserAndStatus(
                userEntity,
                ACTIVE.name(),
                PageRequest.of(page,size, Sort.by(
                        Sort.Direction.valueOf(direction.toUpperCase()),
                        sort)
                ))).thenReturn(expected);

        List<Order> actual = service.findAll(page, size, sort, direction, userEntity.getUsername());
        Assertions.assertEquals(expected,actual);
    }
}