package com.epam.esm.validator;

import java.util.Base64;

public class ImageValidator {
    private ImageValidator(){}

    public static byte[] getByteFromImage(String image) {
        if(image == null || image.isBlank()) {
            return null;
        }
        return Base64.getDecoder().decode(image);
    }

    public static String getStringFromImage(byte[] image) {
        if(isBytesNull(image)) {
            return null;
        }
        return Base64.getEncoder().encodeToString(image);
    }

    public static boolean isBytesNull(byte[] image) {
        return image == null || image.length <= 0;
    }
}
