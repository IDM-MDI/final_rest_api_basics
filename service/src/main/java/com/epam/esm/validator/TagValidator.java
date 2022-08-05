package com.epam.esm.validator;

import com.epam.esm.entity.Tag;

import java.util.List;

public class TagValidator {
    private TagValidator(){}
    public static boolean isTagEqualsToDB(Tag tagFromDB, Tag tag) {
        return tagFromDB != null && tag != null && tagFromDB.getName().equals(tag.getName());
    }

    public static boolean isListTagEmpty(List<Tag> tagList) {
        return tagList == null || tagList.isEmpty() || !isTagInsideListNotEmpty(tagList);
    }
    private static boolean isTagInsideListNotEmpty(List<Tag> tagList) {
        for (Tag tag : tagList) {
            if(tag == null) {
                return false;
            }
        }
        return true;
    }
}

