package com.epam.esm.validator;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GiftValidator {

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

    public static boolean isTagListNotEmpty(List<Tag> tagList) {
        boolean result = false;
        for (Tag tag : tagList) {
            if (tag != null) {
                result = true;
                break;
            }
        }
        return result;
    }

    private static boolean isGiftByTag(List<Tag> giftTags, List<Tag> searchTags) {
        boolean result = false;
        for (Tag i: giftTags) {
            for (Tag j: searchTags) {
                if (i.getName().equals(j.getName())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public static void uniteEntities(GiftCertificate first, GiftCertificate second) {
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
        if(first.getTagList() == null || first.getTagList().size() == 0) {
            first.setTagList(second.getTagList());
        }
    }
}
