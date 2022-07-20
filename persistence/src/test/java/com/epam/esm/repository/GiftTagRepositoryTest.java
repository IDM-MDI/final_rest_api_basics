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
class GiftTagRepositoryTest {

    @Autowired
    private GiftCertificateBuilder builder;
    @Autowired
    private GiftCertificateRepository giftRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private GiftTagRepository repository;
    @Autowired
    private StatusRepository statusRepository;

    @Test
    void setDeleteByGift() {
        init();
        long id = repository.findAll()
                .stream()
                .findAny()
                .orElseThrow()
                .getId();

        Status deleted = statusRepository.findByNameIgnoreCase("deleted")
                .orElseThrow();

        repository.setDeleteByGift(id,deleted);
        Assertions.assertEquals(deleted,repository.findById(id).orElseThrow().getStatus());
    }

    public void init() {
        statusRepository.saveAll(List.of(new Status(null,"active"),new Status(null,"deleted")));
        Status active = statusRepository.findByNameIgnoreCase("active")
                .orElseThrow();

        tagRepository.saveAll(List.of(new Tag(null,"testTag1",active),new Tag(null,"testTag2",active),new Tag(null,"testTag3",active)));

        GiftCertificate entity = builder
                .setName("test")
                .setDescription("test")
                .setDuration(1)
                .setPrice(new BigDecimal(1))
                .setCreate_date(LocalDateTime.of(1,1,1,1,1))
                .setUpdate_date(LocalDateTime.of(2,2,2,2,2))
                .setStatus(active)
                .setTagList(tagRepository.findAll())
                .build();
        giftRepository.save(entity);
    }
}