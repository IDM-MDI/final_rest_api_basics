package com.epam.esm.validator;

import com.epam.esm.entity.Tag;

public class TagValidator {
    private TagValidator(){}
    public static boolean isTagEqualsToDB(Tag tagFromDB, Tag tag) {
        return tagFromDB != null && tag != null && tagFromDB.getName().equals(tag.getName());
    }
}

