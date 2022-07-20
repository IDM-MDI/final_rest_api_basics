package com.epam.esm.repository;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.OrderBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.config.PersistenceConfig;
import com.epam.esm.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest()
@ContextConfiguration(classes= PersistenceConfig.class)
@ComponentScan(basePackages = "com.epam.esm")
class OrderRepositoryTest {
    private static final Random random = new Random();
    private static final int min = 1;
    private static final int max = 3;
    @Autowired
    private GiftCertificateBuilder giftBuilder;
    @Autowired
    private UserBuilder userBuilder;
    @Autowired
    private GiftCertificateRepository giftRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OrderRepository repository;

    private void init() {
        statusRepository.saveAll(new ArrayList<>(List.of(new Status(null,"active"),new Status(null,"deleted"))));
        Status active = statusRepository.findByNameIgnoreCase("active")
                .orElseThrow();

        tagRepository.saveAll(new ArrayList<>(List.of(new Tag(null,"testTag1",active),new Tag(null,"testTag2",active),new Tag(null,"testTag3",active))));
        createOrder();
    }

    private void createOrder() {
        Status active = statusRepository.findByNameIgnoreCase("active")
                .orElseThrow();
        OrderBuilder builder = new OrderBuilder();
        if(userRepository.count() < 3 || giftRepository.count() < 3) {
            createUsers();
            createGifts();
        }
        for (int i = 1; i < 4; i++) {
            GiftCertificate gift = giftRepository.findByName("testGift" + i).orElseThrow();
            User user = userRepository.findUserByUsername("testUser" + i).orElseThrow();
            Order order = builder.setUser(user)
                    .setGift(gift)
                    .setPrice(gift.getPrice())
                    .setStatus(active)
                    .build();
            user.setOrders(new ArrayList<>(List.of(repository.save(order))));
            userRepository.save(user);
        }
    }

    private void createUsers() {
        Status active = statusRepository.findByNameIgnoreCase("active")
                .orElseThrow();
        roleRepository.save(new Role(0,"user"));
        Role userRole = roleRepository.findRoleByName("user").orElseThrow();
        for (int i = 1; i < 4; i++) {
            User user = userBuilder.setName("testUser" + i)
                    .setPassword("testPassword")
                    .setRoles(new ArrayList<>(List.of(userRole)))
                    .setStatus(active)
                    .build();
            userRepository.save(user);
        }
    }

    private void createGifts() {
        Status active = statusRepository.findByNameIgnoreCase("active")
                .orElseThrow();
        for (int i = 1; i < 4; i++) {
            int randomNumber1 = random.nextInt((max - min) + 1) + min;
            int randomNumber2 = random.nextInt((max - min) + 1) + min;
            List<Tag> tags = new ArrayList<>(List.of(
                    tagRepository.findByName("testTag" + randomNumber1)
                            .orElseThrow(),
                    tagRepository.findByName("testTag" + randomNumber2)
                            .orElseThrow()
            ));
            GiftCertificate entity = giftBuilder
                    .setName("testGift" + i)
                    .setDescription("test" + i)
                    .setDuration(i)
                    .setPrice(new BigDecimal(i))
                    .setCreate_date(LocalDateTime.of(i,i,i,i,i))
                    .setUpdate_date(LocalDateTime.of(i+1,i+1,i+1,i+1,i+1))
                    .setStatus(active)
                    .setTagList(tags)
                    .build();
            giftRepository.save(entity);
        }
    }

    @Test
    void getTop() {
        init();
        List<User> top = repository.getTop();
        assertTrue(isTop(top));
    }

    private boolean isTop(List<User> top) {
        boolean result = true;
        int prevPrice = Integer.MAX_VALUE;
        int price = 0;
        for (User user : top) {
            for (Order order : user.getOrders()) {
                price += order.getPrice().intValue();
            }
            if(price > prevPrice) {
                result = false;
            }
            prevPrice = price;
            price = 0;
        }
        return result;
    }

    @Test
    void setDelete() {
        init();
        Status expected = statusRepository.findByNameIgnoreCase("deleted").orElseThrow();
        long id = repository.findAll().stream().findAny().orElseThrow().getId();

        repository.setDelete(id,expected);

        Status actual = repository.findById(id).orElseThrow().getStatus();
        assertEquals(expected,actual);
    }
}