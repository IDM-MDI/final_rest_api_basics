package com.epam.esm.validator;

import java.util.List;

public class ListValidator {
    private ListValidator(){}

    public static boolean isListEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
