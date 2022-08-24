package com.epam.esm.repository;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.config.PersistenceConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
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

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.entity.StatusName.DELETED;


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

    @Test
    void setDeleteByGift() {
        init();
        long id = repository.findAll()
                .stream()
                .findAny()
                .orElseThrow()
                .getId();

        repository.setDeleteByGift(id,DELETED.name());

        GiftTag actual = repository.findById(id).orElseThrow();

        Assertions.assertEquals(null, actual.getStatus());
    }

    public void init() {
        tagRepository.saveAll(
                List.of(
                        new Tag(null,"testTag1",null,ACTIVE.name()),
                        new Tag(null,"testTag2",null,ACTIVE.name()),
                        new Tag(null,"testTag3",null,ACTIVE.name())
                )
        );

        GiftCertificate entity = builder
                .setName("test")
                .setDescription("test")
                .setDuration(1)
                .setPrice(new BigDecimal(1))
                .setCreateDate(LocalDateTime.of(1,1,1,1,1))
                .setUpdateDate(LocalDateTime.of(2,2,2,2,2))
                .setStatus(ACTIVE.name())
                .setTagList(tagRepository.findAll())
                .build();
        giftRepository.save(entity);
    }
}