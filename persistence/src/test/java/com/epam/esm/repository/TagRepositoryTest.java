package com.epam.esm.repository;

import com.epam.esm.config.PersistenceConfig;
import com.epam.esm.entity.Tag;
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

@DataJpaTest()
@ContextConfiguration(classes= PersistenceConfig.class)
@ComponentScan(basePackages = "com.epam.esm")
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    public void init() {
        tagRepository.saveAll(
                List.of(
                        new Tag(null,"testTag1",ACTIVE.name()),
                        new Tag(null,"testTag2",ACTIVE.name())
                )
        );
    }

    @Test
    void findByName() {
        init();
        String expected = "testTag1";
        assertEquals(expected,tagRepository.findByName(expected).orElseThrow().getName());
    }

    @Test
    void setDelete() {
        init();
        Tag tag = tagRepository.findByName("testTag2").orElseThrow();
        tagRepository.setDelete(tag.getId(),DELETED.name());
        assertEquals(
                tagRepository.findByName("testTag2")
                                .orElseThrow()
                                .getStatus(),
                DELETED.name()
        );
    }

    @Test
    void findByStatus() {
        init();
        assertEquals(
                tagRepository.findByStatus(ACTIVE.name(), PageRequest.of(0, 1))
                                .stream()
                                .findAny()
                                .orElseThrow()
                                .getStatus(),
                ACTIVE.name()
        );
    }
}