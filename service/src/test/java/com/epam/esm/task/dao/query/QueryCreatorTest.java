package com.epam.esm.task.dao.query;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class QueryCreatorTest {
    private static final QueryCreator creator = new QueryCreator();
    private static final String tableName = "tag";
    private static final List<String> columns = new ArrayList<>();

    @BeforeAll
    public static void init() {
        columns.add("id");
        columns.add("name");
    }

    @Test
    void delete() {
        String actual = creator.delete(tableName);
        String expected = "UPDATE tag SET deleted = 1 WHERE id = ?;";
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void update() {
        String actual = creator.update(tableName,columns);
        String expected = "UPDATE tag SET name = ? WHERE id = ? AND deleted = 0;";
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void insert() {
        String actual = creator.insert(tableName,columns);
        String expected = "INSERT INTO tag(name) VALUES(?);";
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findAll() {
        String actual = creator.findAll(tableName);
        String expected = "SELECT * FROM tag WHERE deleted = 0;";
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findById() {
        String actual = creator.findById(tableName);
        String expected = "SELECT * FROM tag WHERE id = ? AND deleted = 0;";
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findByParam() {
        Map<String,String> param = new HashMap<>();
        param.put("id","1");
        param.put("name","test");
        String actual = creator.findByParam(tableName, param);
        String expected = "SELECT * FROM tag WHERE name='test' AND id='1' AND deleted = 0;";
        Assertions.assertEquals(expected,actual);
    }
}