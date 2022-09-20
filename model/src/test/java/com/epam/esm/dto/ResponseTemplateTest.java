package com.epam.esm.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.epam.esm.dto.ResponseTemplate.PAGE;
import static org.junit.jupiter.api.Assertions.*;

class ResponseTemplateTest {

    @Test
    void pageResponseTemplate() {
        int page = 0;
        int size = 0;
        String entityName = "Test";
        String sort = "id";
        String direction = "asc";

        String expected = entityName + PAGE +
                "page - " + page +
                ", size - " + size +
                ", sort -" + sort +
                ", direction - " + direction;
        String actual = ResponseTemplate.pageResponseTemplate(entityName, page, size, sort, direction);
        Assertions.assertEquals(expected,actual);
    }
}