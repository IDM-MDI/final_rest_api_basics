package com.epam.esm.service.impl;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.EntityService;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;
import static com.epam.esm.validator.OrderValidator.isUserAndGiftEmpty;
import static com.epam.esm.entity.StatusName.*;


@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class OrderService implements EntityService<Order,OrderDto> {

    private final OrderRepository repository;
    private final GiftCertificateRepository giftRepository;
    private final StatusService statusService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final GiftCertificateModelMapper giftMapper;

    @Autowired
    public OrderService(OrderRepository repository,
                        GiftCertificateRepository giftRepository,
                        StatusService statusService, UserRepository userRepository,
                        UserService userService,
                        GiftCertificateModelMapper giftMapper) {
        this.repository = repository;
        this.giftRepository = giftRepository;
        this.statusService = statusService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.giftMapper = giftMapper;
    }

    public DtoPage<OrderDto> saveByUserWithDtoPage(String username, long id) throws RepositoryException {
        UserDto userByUsername = userService.findUserByUsername(username);
        OrderDto dto = new OrderDto();
        dto.setGiftId(id);
        dto.setUserId(userByUsername.getId());
        return new DtoPageBuilder<OrderDto>()
                .setContent(List.of(mapper(save(dto))))
                .build();
    }

    @Override
    public Order save(OrderDto dto) throws RepositoryException {
        Optional<User> userOptional = userRepository.findById(dto.getUserId());
        Optional<GiftCertificate> giftOptional = giftRepository.findById(dto.getGiftId());

        if(isUserAndGiftEmpty(userOptional,giftOptional)) {
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());
        }

        return repository.save(createOrder(
                                userOptional.get(),
                                giftOptional.get()));
    }

    @Override
    public Order update(OrderDto dto) throws RepositoryException {
        return null;
    }

    @Override
    public List<Order> findAll(int page, int size, String sort) throws RepositoryException {
        return null;
    }

    @Override
    public Order findById(long id) throws RepositoryException {
        return null;
    }

    @Override
    public List<Order> findByParam(OrderDto dto) throws RepositoryException {
        return null;
    }

    @Override
    public void delete(long id) throws RepositoryException {
        repository.setDelete(id,statusService.findStatus(DELETED.name()));
    }

    @Override
    public List<Order> findActive() {
        return null;
    }

    @Override
    public List<Order> findDeleted() {
        return null;
    }

    @Override
    public List<Order> findByStatus(String statusName) {
        return null;
    }

    private Order createOrder(User user, GiftCertificate gift) {
        Order order = new Order();
        order.setUser(user);
        order.setGift(gift);
        order.setPrice(gift.getPrice());
        return order;
    }
    private OrderDto mapper(Order order) {
        return new OrderDto(
                order.getId(),
                order.getPrice(),
                order.getPurchaseTime(),
                giftMapper.toDto(order.getGift()),
                order.getGift().getId(),
                order.getUser().getId()
        );
    }
}
