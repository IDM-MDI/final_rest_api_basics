package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.StatusName.DELETED;
import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;
import static com.epam.esm.validator.OrderValidator.isUserAndGiftEmpty;


@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class OrderServiceImpl implements OrderService {
    public static final String ORDER = "Order ";
    private final OrderRepository repository;
    private final GiftCertificateRepository giftRepository;
    private final UserRepository userRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository repository,
                            GiftCertificateRepository giftRepository,
                            UserRepository userRepository) {
        this.repository = repository;
        this.giftRepository = giftRepository;
        this.userRepository = userRepository;
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
    public Order update(OrderDto dto) {
        return null;
    }

    @Override
    public List<Order> findAll(int page, int size, String sort) {
        return Collections.emptyList();
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
    public List<Order> findByStatus(int page, int size, String sort, String statusName) {
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
