package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;

import java.util.Optional;

public class OrderValidator {

    public static boolean isUserAndGiftEmpty(Optional<User> user, Optional<GiftCertificate> gift) {
        return user.isEmpty() &&
                gift.isEmpty();
    }
}
