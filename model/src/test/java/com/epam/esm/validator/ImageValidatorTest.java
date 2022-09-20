package com.epam.esm.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ImageValidatorTest {

    @Test
    void getByteFromImageShouldDecode() {
        byte[] expected = new byte[]{-41, 93, 117, -41};
        byte[] actual = ImageValidator.getByteFromImage("111111");
        Assertions.assertEquals(Arrays.toString(expected),Arrays.toString(actual));
    }

    @Test
    void getByteFromImageShouldNullByNull() {
        Assertions.assertNull(ImageValidator.getByteFromImage(null));
    }

    @Test
    void getByteFromImageShouldNullByStringBlank() {
        Assertions.assertNull(ImageValidator.getByteFromImage(""));
    }

    @Test
    void getStringFromImageShouldEncode() {
        String expected = "11111w==";
        String actual = ImageValidator.getStringFromImage(new byte[]{-41, 93, 117, -41});
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void getStringFromImageShouldNull() {
        Assertions.assertNull(ImageValidator.getStringFromImage(null));
    }

    @Test
    void isBytesNullShouldFalseByLength() {
        Assertions.assertFalse(ImageValidator.isBytesNotNull(new byte[]{}));
    }

    @Test
    void isBytesNullShouldTrue() {
        Assertions.assertTrue(ImageValidator.isBytesNotNull(new byte[]{1,1,1,1}));
    }
}