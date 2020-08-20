package org.thehellnet.utility;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public final class StringUtility {

    private static final Logger logger = LoggerFactory.getLogger(StringUtility.class);

    private StringUtility() {
    }

    public static List<String> splitLines(String rawText) {
        return getStrings(rawText, "\\R");
    }

    public static String joinLines(List<String> lines) {
        return getString(lines, "\n");
    }

    public static List<String> splitSpaces(String rawText) {
        if (rawText == null) {
            return null;
        }

        String cleanRawText = rawText;
        cleanRawText = cleanRawText.replaceAll("\\R", "");
        cleanRawText = cleanRawText.replaceAll("\"", "");
        return getStrings(cleanRawText, "\\s+");
    }

    public static String joinSpaces(List<String> items) {
        return getString(items, " ");
    }

    public static String firstLetterLowercase(String input) {
        if (input == null) {
            return null;
        }

        if (input.length() == 0) {
            return "";
        }

        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    public static String randomString(int seed, int offset, int length) {
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            return null;
        }

        SecureRandom secureRandom = new SecureRandom();

        byte[] bytes = secureRandom.generateSeed(seed);
        String inputString = new String(bytes);
        String data = PasswordUtility.newInstance().hash(inputString);
        messageDigest.update(data.getBytes());

        byte[] digest = messageDigest.digest();
        String hexString = Hex.encodeHexString(digest);
        return String.valueOf(hexString.subSequence(offset, offset + length));
    }

    private static List<String> getStrings(String rawText, String regex) {
        if (rawText == null) {
            return Collections.emptyList();
        }

        List<String> lines = new ArrayList<>();

        String[] rawLines = rawText.trim().split(regex);
        for (String rawLine : rawLines) {
            String line = rawLine.trim();
            line = line.replaceAll("^'", "");
            line = line.replaceAll("'$", "");
            if (line.length() > 0) {
                lines.add(line);
            }
        }

        return lines;
    }

    private static String getString(List<String> lines, String delimiter) {
        if (lines == null) {
            return "";
        }

        if (lines.isEmpty()) {
            return "";
        }

        StringJoiner stringJoiner = new StringJoiner(delimiter);

        for (String line : lines) {
            stringJoiner.add(line.trim());
        }

        return stringJoiner.toString();
    }
}
