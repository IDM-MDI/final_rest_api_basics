package com.epam.esm.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

class SortValidatorTest {

    @Test
    void getValidSortByASC() {
        String id = "id";
        String direction = "asc";
        Sort expected = Sort.by(id).ascending();
        Sort actual = SortValidator.getValidSort(id, direction);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void getValidSortByDESC() {
        String id = "id";
        String direction = "desc";
        Sort expected = Sort.by(id).descending();
        Sort actual = SortValidator.getValidSort(id, direction);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void isDirectionDescShouldTrue() {
        Assertions.assertTrue(SortValidator.isDirectionDesc("desc"));
    }

    @Test
    void isDirectionDescShouldFalseByNull() {
        Assertions.assertFalse(SortValidator.isDirectionDesc(null));
    }

    @Test
    void isDirectionDescShouldFalseByASC() {
        Assertions.assertFalse(SortValidator.isDirectionDesc("asc"));
    }
}