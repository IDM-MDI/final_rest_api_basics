package com.epam.esm.util;

import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;

public class HashGenerator {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);

    @SneakyThrows
    public static String generateByMD5(String password) {
        StringBuilder sb = new StringBuilder();
        MessageDigest md5 = null;
        md5 = MessageDigest.getInstance("md5");
        byte[] bytes = md5.digest(password.getBytes());
        for (byte i: bytes) {
            sb.append(String.format("%02X",i));
        }
        return sb.toString();
    }

    public static String generateHash(String password) {
        return encoder.encode(generateByMD5(password));
    }

    public static BCryptPasswordEncoder getEncoder() {
        return encoder;
    }
}
