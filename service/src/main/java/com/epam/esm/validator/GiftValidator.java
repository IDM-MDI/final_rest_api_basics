package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GiftValidator {

    private GiftValidator(){}

    public static List<GiftCertificate> findEquals(List<GiftCertificate> first, List<GiftCertificate> second) {
        List<GiftCertificate> result = new ArrayList<>();
        for (GiftCertificate i : first) {
            for (GiftCertificate j : second) {
                if(Objects.equals(i.getId(), j.getId())) {
                    result.add(i);
                }
            }
        }
        return result;
    }

    public static boolean isTagsEmpty(String tags) {
        return tags.trim().isEmpty() || tags.isBlank();
    }

    public static boolean isGiftEmpty(GiftCertificate entity) {
        return entity.getId() == null &&
                (entity.getName() == null || entity.getName().trim().isEmpty() || entity.getName().trim().isBlank()) &&
                entity.getPrice() == null &&
                (entity.getDescription() == null || entity.getDescription().trim().isEmpty() || entity.getDescription().trim().isBlank());
    }

    public static void uniteEntities(GiftCertificate first, GiftCertificate second) {       //TODO: REMAKE THIS METHOD
        if(first.getId() == null) {
            first.setId(second.getId());
        }
        if(first.getName() == null ||first.getName().trim().isEmpty()) {
            first.setName(second.getName());
        }
        if(first.getDescription() == null || first.getDescription().trim().isEmpty()) {
            first.setDescription(second.getDescription());
        }
        if(first.getPrice() == null) {
            first.setPrice(second.getPrice());
        }
        if(first.getDuration() == null) {
            first.setDuration(second.getDuration());
        }
        if(first.getTagList() == null || first.getTagList().isEmpty()) {
            first.setTagList(second.getTagList());
        }
    }
}
