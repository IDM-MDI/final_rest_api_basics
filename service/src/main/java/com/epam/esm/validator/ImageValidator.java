package com.epam.esm.validator;

import org.springframework.web.multipart.MultipartFile;

public class ImageValidator {
    private static final String IMG_REGEX = "image.+";

    private ImageValidator(){}

    public static boolean isImageValid(MultipartFile image) {
        return isImageNotNull(image) && image.getContentType() != null && image.getContentType().matches(IMG_REGEX);
    }

    public static boolean isImageNotNull(MultipartFile image) {
        return image != null;
    }
}
