package org.thehellnet.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public final class StringUtility {

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

        return getStrings(rawText.replaceAll("\\R", ""), "\\s+");
    }

    public static String joinSpaces(List<String> items) {
        return getString(items, " ");
    }

    private static List<String> getStrings(String rawText, String regex) {
        if (rawText == null) {
            return null;
        }

        List<String> lines = new ArrayList<>();

        String[] rawLines = rawText.trim().split(regex);
        for (String rawLine : rawLines) {
            String line = rawLine.trim();
            if (line.length() > 0) {
                lines.add(line);
            }
        }

        return lines;
    }

    private static String getString(List<String> lines, String delimiter) {
        if (lines == null) {
            return null;
        }

        if (lines.size() == 0) {
            return "";
        }

        StringJoiner stringJoiner = new StringJoiner(delimiter);

        for (String line : lines) {
            stringJoiner.add(line.trim());
        }

        return stringJoiner.toString();
    }
}
