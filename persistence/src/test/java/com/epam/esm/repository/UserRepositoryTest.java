package com.epam.esm.repository;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.config.PersistenceConfig;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest()
@ContextConfiguration(classes= PersistenceConfig.class)
@ComponentScan(basePackages = "com.epam.esm")
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;


    @Autowired
    private UserBuilder builder;

    @Autowired
    private GiftCertificateBuilder giftBuilder;

    public void init() {
        roleRepository.save(new Role(0,"user"));

        User user = builder.setName("testName")
                .setPassword("testPassword")
                .setRoles(List.of(roleRepository.findRoleByName("user").orElseThrow()))
                .setStatus(ACTIVE.name())
                .build();
        repository.save(user);
    }

    @Test
    void setDelete() {
        init();
        User user = repository.findUserByUsername("testName").orElseThrow();
        repository.setDelete(user.getId(), DELETED.name());
        assertEquals(DELETED.name(),repository.getById(user.getId()).getStatus());
    }

    @Test
    void findUsersByOrdersEmpty() {
        init();
        assertNull(repository.findUsersByOrdersEmpty().stream().findAny().orElseThrow().getOrders());
    }

    @Test
    void findByStatus() {
        init();
        User user = repository.findByStatus(ACTIVE.name(),PageRequest.of(0, 1)).stream().findAny().orElseThrow();
        assertEquals(ACTIVE.name(),user.getStatus());
    }

    @Test
    void findUserByUsername() {
        init();
        String expected = "testName";
        assertEquals(expected,repository.findUserByUsername("testName").orElseThrow().getUsername());
    }


    @Test
    void existsByUsername() {
        init();
        boolean expected = true;
        Assertions.assertTrue(repository.existsByUsername("testName"));
    }

}