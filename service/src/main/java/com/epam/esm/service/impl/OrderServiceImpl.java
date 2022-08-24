package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Collections;
import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;


@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class OrderServiceImpl implements OrderService {
    public static final String ORDER = "Order ";
    private final OrderRepository repository;
    private final GiftCertificateServiceImpl giftService;
    private final UserServiceImpl userService;


    @Autowired
    public OrderServiceImpl(OrderRepository repository, GiftCertificateServiceImpl giftService, UserServiceImpl userService) {
        this.repository = repository;
        this.giftService = giftService;
        this.userService = userService;
    }



    @Override
    public Order save(OrderDto dto) throws RepositoryException {
        User user = userService.findById(dto.getUserId());
        GiftCertificate gift = giftService.findById(dto.getGiftId());
        return repository.save(createOrder(user,gift));
    }

    @Override
    public Order update(OrderDto dto) {
        return null;
    }

    @Override
    public List<Order> findAll(int page, int size, String sort, String direction) {
        return Collections.emptyList();
    }

    public List<Order> findAll(int page, int size, String sort, String direction,String username) {
        User user = userService.findUserByUsername(username);
        return repository.findOrdersByUserAndStatus(
                user,
                ACTIVE.name(),
                PageRequest.of(page,size, Sort.by(
                        Sort.Direction.valueOf(direction.toUpperCase()),
                        sort)
                )
        );
    }

    @Override
    public Order findById(long id) {
        return null;
    }

    @Override
    public List<Order> findByParam(OrderDto dto) {
        return Collections.emptyList();
    }

    @Override
    public void delete(long id) throws RepositoryException {
        repository.setDelete(id, DELETED.name());
    }

    @Override
    public List<Order> findByStatus(int page, int size, String sort, String direction, String statusName) {
        return Collections.emptyList();
    }

    private Order createOrder(User user, GiftCertificate gift) {
        Order order = new Order();
        order.setUser(user);
        order.setGift(gift);
        order.setPrice(gift.getPrice());
        return order;
    }

}
