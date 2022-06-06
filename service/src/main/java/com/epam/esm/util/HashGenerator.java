package com.epam.esm.util;

import lombok.SneakyThrows;

import java.security.MessageDigest;

public class HashGenerator {

    @SneakyThrows
    public static String generatePassword(String password) {
        StringBuilder sb = new StringBuilder();
        MessageDigest md5 = null;
        md5 = MessageDigest.getInstance("md5");
        byte[] bytes = md5.digest(password.getBytes());
        for (byte i: bytes) {
            sb.append(String.format("%02X",i));
        }
        return sb.toString();
    }
}
