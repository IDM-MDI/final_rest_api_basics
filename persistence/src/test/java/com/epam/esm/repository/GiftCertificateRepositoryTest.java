package com.epam.esm.repository;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.config.PersistenceConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;

@DataJpaTest()
@ContextConfiguration(classes= PersistenceConfig.class)
@ComponentScan(basePackages = "com.epam.esm")
class GiftCertificateRepositoryTest {

    @Autowired
    private GiftCertificateBuilder builder;

    @Autowired
    private GiftCertificateRepository repository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void setDelete() {
        init();

        repository.setDelete(repository.findByName("test")
                                            .orElseThrow()
                                            .getId(), DELETED.name());

        GiftCertificate actual = repository.findByName("test")
                .orElseThrow();

        Assertions.assertEquals(DELETED.name(),actual.getStatus());
    }

    @Test
    void findByName() {
        String name = "test";
        init();
        GiftCertificate actual = repository.findByName(name)
                .orElseThrow();
        Assertions.assertEquals(name,actual.getName());
    }

    @Test
    void findByTagListIn() {
        init();
        Tag expected = tagRepository.findByName("testTag1")
                                .orElseThrow();
        GiftCertificate actualGift = repository.findByTagListIn(List.of(expected))
                                            .stream()
                                            .findAny()
                                            .orElseThrow();
        Tag actual = actualGift.getTagList()
                .stream()
                .filter(tag -> tag.getName().equals(expected.getName()))
                .findAny()
                .orElseThrow();
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findByStatus() {
        init();
        GiftCertificate actual = repository.findByStatus(ACTIVE.name(),PageRequest.of(0, 1))
                                            .stream()
                                            .findAny()
                                            .orElseThrow();
        Assertions.assertEquals(ACTIVE.name(),actual.getStatus());
    }

    private void init() {
        tagRepository.saveAll(
                List.of(
                new Tag(null,"testTag1",ACTIVE.name()),
                new Tag(null,"testTag2",ACTIVE.name()),
                new Tag(null,"testTag3",ACTIVE.name())
                )
        );

        GiftCertificate entity = builder
                .setId(1L)
                .setName("test")
                .setDescription("test")
                .setDuration(1)
                .setPrice(new BigDecimal(1))
                .setCreateDate(LocalDateTime.of(1,1,1,1,1))
                .setUpdateDate(LocalDateTime.of(2,2,2,2,2))
                .setStatus(ACTIVE.name())
                .setTagList(tagRepository.findAll())
                .build();
        repository.save(entity);
    }
}