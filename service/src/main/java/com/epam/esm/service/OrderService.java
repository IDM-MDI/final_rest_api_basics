package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class OrderService {

    private final OrderRepository repository;
    private final GiftCertificateRepository giftRepository;
    private final UserRepository userRepository;


    @Autowired
    public OrderService(OrderRepository repository, GiftCertificateRepository giftRepository, UserRepository userRepository) {
        this.repository = repository;
        this.giftRepository = giftRepository;
        this.userRepository = userRepository;
    }

    public void save(OrderDto dto) throws RepositoryException {
        Optional<User> userOptional = userRepository.findById(dto.getUserId());
        Optional<GiftCertificate> giftOptional = giftRepository.findById(dto.getGiftId());
        if(userOptional.isPresent() && giftOptional.isPresent()) {
            Order order = new Order();
            order.setUser(userOptional.get());
            order.setGift(giftOptional.get());
            order.setPrice(giftOptional.get().getPrice());
            repository.save(order);
        }
        else
            throw new RepositoryException();
    }
}
