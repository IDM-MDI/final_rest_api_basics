package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;

import java.util.Optional;

public class OrderValidator {

    public static boolean isUserAndGiftPresent(Optional<User> user, Optional<GiftCertificate> gift) {
        return user.isPresent() && gift.isPresent();
    }
}
