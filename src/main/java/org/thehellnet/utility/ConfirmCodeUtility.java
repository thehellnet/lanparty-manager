package org.thehellnet.utility;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class ConfirmCodeUtility {

    public static final int LENGTH = 64;

    private static final int OFFSET = 17;

    private static final Logger logger = LoggerFactory.getLogger(ConfirmCodeUtility.class);

    public static String generate() {
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            return null;
        }

        SecureRandom secureRandom = new SecureRandom();

        byte[] bytes = secureRandom.generateSeed(2048);
        String inputString = new String(bytes);
        String data = PasswordUtility.newInstance().hash(inputString);
        messageDigest.update(data.getBytes());

        byte[] digest = messageDigest.digest();
        String hexString = Hex.encodeHexString(digest);
        return String.valueOf(hexString.subSequence(OFFSET, OFFSET + LENGTH));
    }
}
