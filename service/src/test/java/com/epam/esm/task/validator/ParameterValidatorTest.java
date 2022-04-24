package com.epam.esm.task.validator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParameterValidatorTest {

    @Test
    void getValidMap() {
        List<String> validColumns = new ArrayList<>();
        validColumns.add("id");
        validColumns.add("name");

        Map<String,String> notValidMap = new HashMap<>();
        notValidMap.put("id","1");
        notValidMap.put("name","test");
        notValidMap.put("test","notValid");

        Map<String,String> expected = new HashMap<>();
        expected.put("id","1");
        expected.put("name","test");

        Map<String,String> actual = ParameterValidator.getValidMap(notValidMap,validColumns);
        assertEquals(actual,expected);
    }
}