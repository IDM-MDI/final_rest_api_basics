package com.epam.esm.hateoas.impl;

import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.ControllerType;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.TagDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;

@SpringBootTest(classes = WebApplication.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test","prod"})
class TagHateoasTest {
    private static final int PAGE = 1;
    private static final int SIZE = 0;
    private static final String SORT = "ID";
    private static final String DIRECTION = "asc";

    private static final List<TagDto> dtoList = List.of(
            new TagDto(1L,"testTag1",null,false,ACTIVE.name()),
            new TagDto(2L,"testTag2",null,false,ACTIVE.name()),
            new TagDto(3L,"testTag3",null,false,ACTIVE.name())
            );

    @Autowired
    private TagHateoas hateoas;

    @SneakyThrows
    @Test
    void setHateoasTagAll() {
        DtoPage<TagDto> expected = new DtoPage<>(dtoList,null,SIZE,PAGE,SORT,DIRECTION,false,null, ControllerType.TAG_ALL);
        hateoas.setHateoas(expected);
        Assertions.assertTrue(isLinksNotEmpty(dtoList));
    }
    @SneakyThrows
    @Test
    void setHateoasTagTop() {
        DtoPage<TagDto> expected = new DtoPage<>(dtoList,null,SIZE,PAGE,SORT,DIRECTION,false,null, ControllerType.TAG_TOP);
        hateoas.setHateoas(expected);
        Assertions.assertTrue(isLinksNotEmpty(dtoList));
    }

    public boolean isLinksNotEmpty(List<TagDto> list) {
        for (TagDto i : list) {
            if(i.getLinks().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}