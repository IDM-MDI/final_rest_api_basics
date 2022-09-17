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

        repository.setDelete(repository.findByName("test1")
                                            .orElseThrow()
                                            .getId(), DELETED.name());

        GiftCertificate actual = repository.findByName("test1")
                .orElseThrow();

        Assertions.assertEquals(DELETED.name(),actual.getStatus());
    }

    @Test
    void findByName() {
        init();
        String name = "test1";
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
        GiftCertificate actual = repository.findGiftCertificatesByStatus(ACTIVE.name(),PageRequest.of(0, 1))
                                            .stream()
                                            .findAny()
                                            .orElseThrow();
        Assertions.assertEquals(ACTIVE.name(),actual.getStatus());
    }

    @Test
    void findByTagListInPageable() {
        init();
        Tag expectedTag = tagRepository.findByName("testTag1")
                                    .orElseThrow();

        GiftCertificate expected = builder
                                        .setName("test1")
                                        .setDescription("test1")
                                        .setDuration(1)
                                        .setPrice(new BigDecimal(1))
                                        .setStatus(ACTIVE.name())
                                        .setTagList(tagRepository.findAll())
                                        .build();

        GiftCertificate actual = repository.findByTagListIn(List.of(expectedTag),PageRequest.of(0,1))
                                                 .stream()
                                                 .findAny()
                                                 .orElseThrow();
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void findByTagListInAndStatus() {
        init();
        Tag expectedTag = tagRepository.findByName("testTag1")
                                       .orElseThrow();

        GiftCertificate expected = builder
                .setId(2L)
                .setName("test2")
                .setDescription("test2")
                .setDuration(2)
                .setPrice(new BigDecimal(2))
                .setStatus(DELETED.name())
                .setTagList(tagRepository.findAll())
                .build();

        GiftCertificate actual = repository.findByTagListInAndStatus(List.of(expectedTag), DELETED.name(), PageRequest.of(0, 3))
                .stream()
                .findAny()
                .orElseThrow();

        actual.setCreateDate(null);
        actual.setUpdateDate(null);

        Assertions.assertEquals(expected.getName(),actual.getName());
    }

    private void init() {
        tagRepository.saveAll(
                List.of(
                new Tag(null,"testTag1",null,ACTIVE.name()),
                new Tag(null,"testTag2",null,ACTIVE.name()),
                new Tag(null,"testTag3",null,ACTIVE.name())
                )
        );
        repository.saveAll(List.of(builder
                        .setId(1L)
                        .setName("test1")
                        .setDescription("test1")
                        .setDuration(1)
                        .setPrice(new BigDecimal(1))
                        .setCreateDate(LocalDateTime.of(1, 1, 1, 1, 1))
                        .setUpdateDate(LocalDateTime.of(2, 2, 2, 2, 2))
                        .setStatus(ACTIVE.name())
                        .setTagList(tagRepository.findAll())
                        .build(),
                builder
                        .setId(2L)
                        .setName("test2")
                        .setDescription("test2")
                        .setDuration(2)
                        .setPrice(new BigDecimal(2))
                        .setCreateDate(LocalDateTime.of(1, 1, 1, 1, 1))
                        .setUpdateDate(LocalDateTime.of(2, 2, 2, 2, 2))
                        .setStatus(DELETED.name())
                        .setTagList(tagRepository.findAll())
                        .build()));
    }
}