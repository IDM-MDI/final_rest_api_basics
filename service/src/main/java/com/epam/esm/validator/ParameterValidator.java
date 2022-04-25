package com.epam.esm.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterValidator {
    public static final String order = "order";

    public static Map<String,String> getValidMap(Map<String,String> requestParam,
                                                 List<String> columns) {
        Map<String,String> validMap = new HashMap<>();

        for (String i: requestParam.keySet()) {
            for (String j: columns) {
                if(i.equals(j)) validMap.put(i,requestParam.get(i));
                if(i.equals(order)) validMap.put(i,requestParam.get(i));
            }
        }
        return validMap;
    }
}
