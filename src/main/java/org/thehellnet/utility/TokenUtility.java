package org.thehellnet.utility;

import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class TokenUtility {

    public static final int LENGTH = 16;

    private static final int OFFSET = 19;
    private static final int EXIPRATION_DAYS = 1;

    private static final Logger logger = LoggerFactory.getLogger(TokenUtility.class);

    public static String generate() {
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            return null;
        }

        SecureRandom secureRandom = new SecureRandom();

        byte[] bytes = secureRandom.generateSeed(1024);
        String inputString = new String(bytes);
        String data = PasswordUtility.newInstance().hash(inputString);
        messageDigest.update(data.getBytes());

        byte[] digest = messageDigest.digest();
        String hexString = Hex.encodeHexString(digest);
        return String.valueOf(hexString.subSequence(OFFSET, OFFSET + LENGTH));
    }

    public static DateTime generateExpiration(DateTime creationDateTime) {
        return creationDateTime.plusDays(EXIPRATION_DAYS);
    }

    public static boolean validateExpiration(DateTime expirationDateTime) {
        return expirationDateTime.isAfterNow();
    }
}
