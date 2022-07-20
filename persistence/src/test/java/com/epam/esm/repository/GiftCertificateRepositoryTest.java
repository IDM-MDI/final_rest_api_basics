package com.epam.esm.repository;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.config.PersistenceConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest()
@ContextConfiguration(classes= PersistenceConfig.class)
@ComponentScan(basePackages = "com.epam.esm")
class GiftCertificateRepositoryTest {

    @Autowired
    private GiftCertificateBuilder builder;

    @Autowired
    private GiftCertificateRepository repository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void setDelete() {
        init();
        Status deleted = statusRepository.findByNameIgnoreCase("deleted")
                .orElseThrow();

        repository.setDelete(repository.findByName("test")
                                            .orElseThrow()
                                            .getId(), deleted);

        GiftCertificate actual = repository.findByName("test")
                .orElseThrow();

        Assertions.assertEquals(deleted,actual.getStatus());
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
        Status active = statusRepository.findByNameIgnoreCase("active")
                                        .orElseThrow();
        GiftCertificate actual = repository.findByStatus(active)
                                            .stream()
                                            .findAny()
                                            .orElseThrow();
        Assertions.assertEquals(active,actual.getStatus());
    }

    private void init() {
        statusRepository.saveAll(List.of(new Status(null,"active"),new Status(null,"deleted")));
        Status active = statusRepository.findByNameIgnoreCase("active")
                .orElseThrow();

        tagRepository.saveAll(List.of(new Tag(null,"testTag1",active),new Tag(null,"testTag2",active),new Tag(null,"testTag3",active)));

        GiftCertificate entity = builder
                .setId(1L)
                .setName("test")
                .setDescription("test")
                .setDuration(1)
                .setPrice(new BigDecimal(1))
                .setCreate_date(LocalDateTime.of(1,1,1,1,1))
                .setUpdate_date(LocalDateTime.of(2,2,2,2,2))
                .setStatus(active)
                .setTagList(tagRepository.findAll())
                .build();
        repository.save(entity);
    }
}