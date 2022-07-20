package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderValidatorTest {

    @Test
    void isUserAndGiftEmpty() {
        Optional<GiftCertificate> gift = Optional.of(new GiftCertificate());
        Optional<User> user = Optional.of(new User());
        assertFalse(OrderValidator.isUserAndGiftEmpty(user,gift));
    }
}