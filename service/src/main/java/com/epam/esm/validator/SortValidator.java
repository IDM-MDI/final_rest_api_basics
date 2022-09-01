package com.epam.esm.validator;

import org.springframework.data.domain.Sort;

public class SortValidator {
    private SortValidator() {}

    public static Sort getValidSort(String sort, String direction) {
        Sort by = Sort.by(sort);
        return isDirectionDesc(direction)
                ?
                by.descending()
                :
                by.ascending();
    }

    public static boolean isDirectionDesc(String direction) {
        return direction != null && direction.equals("desc");
    }
}
