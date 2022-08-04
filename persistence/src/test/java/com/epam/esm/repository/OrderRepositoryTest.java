package com.epam.esm.repository;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.OrderBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.config.PersistenceConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
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
    private TagRepository tagRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OrderRepository repository;

    private void init() {
        tagRepository.saveAll(
                new ArrayList<>(
                        List.of(
                                new Tag(null,"testTag1",ACTIVE.name()),
                                new Tag(null,"testTag2",ACTIVE.name()),
                                new Tag(null,"testTag3",ACTIVE.name()))
                )
        );
        createOrder();
    }

    private void createOrder() {
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
                    .setStatus(ACTIVE.name())
                    .build();
            user.setOrders(new ArrayList<>(List.of(repository.save(order))));
            userRepository.save(user);
        }
    }

    private void createUsers() {
        roleRepository.save(new Role(0,"user"));
        Role userRole = roleRepository.findRoleByName("user").orElseThrow();
        for (int i = 1; i < 4; i++) {
            User user = userBuilder.setName("testUser" + i)
                    .setPassword("testPassword")
                    .setRoles(new ArrayList<>(List.of(userRole)))
                    .setStatus(ACTIVE.name())
                    .build();
            userRepository.save(user);
        }
    }

    private void createGifts() {
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
                    .setCreateDate(LocalDateTime.of(i,i,i,i,i))
                    .setUpdateDate(LocalDateTime.of(i+1,i+1,i+1,i+1,i+1))
                    .setStatus(ACTIVE.name())
                    .setTagList(tags)
                    .build();
            giftRepository.save(entity);
        }
    }

    @Test
    void getTop() {
        init();
        List<Order> top = repository.getTop(PageRequest.of(0,100));

        assertTrue(isTop(top));
    }

    @Test
    void setDelete() {
        init();
        long id = repository.findAll().stream().findAny().orElseThrow().getId();

        repository.setDelete(id,DELETED.name());

        String actual = repository.findById(id).orElseThrow().getStatus();
        assertEquals(DELETED.name(),actual);
    }
    private boolean isTop(List<Order> top) {
        boolean result = true;
        int prevPrice = Integer.MAX_VALUE;
        int price;
        for (Order order : top) {
            price = order.getPrice().intValue();

            if(price > prevPrice) {
                result = false;
            }
            prevPrice = price;
        }
        return result;
    }
}