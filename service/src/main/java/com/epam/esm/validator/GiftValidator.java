package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;

import java.time.LocalDateTime;
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

    public static boolean isStringEmpty(String tags) {
        return tags == null || tags.trim().isEmpty() || tags.isBlank();
    }

    public static boolean isGiftEmpty(GiftCertificate entity) {
        return entity.getId() == null &&
                (entity.getName() == null || entity.getName().trim().isEmpty() || entity.getName().trim().isBlank()) &&
                entity.getPrice() == null &&
                (entity.getDescription() == null || entity.getDescription().trim().isEmpty() || entity.getDescription().trim().isBlank());
    }

    public static GiftCertificate uniteEntities(GiftCertificate updatable, GiftCertificate fromDB) {
        return new GiftCertificate(
                fromDB.getId(),
                isStringEmpty(updatable.getName())?fromDB.getName():updatable.getName(),
                isStringEmpty(updatable.getDescription())?fromDB.getDescription():updatable.getDescription(),
                updatable.getPrice() == null ? fromDB.getPrice() : updatable.getPrice(),
                updatable.getDuration() == null ? fromDB.getDuration() : updatable.getDuration(),
                fromDB.getCreateDate(),
                LocalDateTime.now(),
                ListValidator.isListEmpty(updatable.getTagList())? fromDB.getTagList() : updatable.getTagList(),
                isStringEmpty(updatable.getShop())? fromDB.getShop() : updatable.getShop(),
                updatable.getMainImage() == null? fromDB.getMainImage() : updatable.getMainImage(),
                updatable.getSecondImage() == null? fromDB.getSecondImage() : updatable.getSecondImage(),
                updatable.getThirdImage() == null? fromDB.getSecondImage() : updatable.getThirdImage(),
                isStringEmpty(updatable.getStatus())? fromDB.getStatus() : updatable.getStatus()
        );
    }
}
