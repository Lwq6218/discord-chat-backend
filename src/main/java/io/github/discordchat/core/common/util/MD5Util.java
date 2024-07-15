package io.github.discordchat.core.common.util;

import lombok.experimental.UtilityClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @classname MD5Util
 * @description TODO
 * @date 2024/6/14
 * @created by lwq
 */
@UtilityClass
public class MD5Util {

    private static final String SALT = "dsdkjejsfvksdsdssdd";

    public static String encrypt(String input) {
        input += SALT;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
