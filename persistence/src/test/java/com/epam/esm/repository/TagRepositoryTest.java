package com.epam.esm.repository;

import com.epam.esm.config.PersistenceConfig;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
@ContextConfiguration(classes= PersistenceConfig.class)
@ComponentScan(basePackages = "com.epam.esm")
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private StatusRepository statusRepository;


    public void init() {
        statusRepository.saveAll(List.of(new Status(null,"active"),new Status(null,"deleted")));
        Status active = statusRepository.findByNameIgnoreCase("active")
                .orElseThrow();
        tagRepository.saveAll(List.of(new Tag(null,"testTag1",active),new Tag(null,"testTag2",active)));
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
        Status deleted = statusRepository.findByNameIgnoreCase("deleted").orElseThrow();
        Tag tag = tagRepository.findByName("testTag2").orElseThrow();
        tagRepository.setDelete(tag.getId(),deleted);
        assertEquals(
                tagRepository.findByName("testTag2")
                                .orElseThrow()
                                .getStatus(),
                deleted
        );
    }

    @Test
    void findByStatus() {
        init();
        Status active = statusRepository.findByNameIgnoreCase("active").orElseThrow();
        assertEquals(
                tagRepository.findByStatus(active)
                                .stream()
                                .findAny()
                                .orElseThrow()
                                .getStatus(),
                active
        );
    }
}