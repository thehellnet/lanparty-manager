package org.thehellnet.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public final class StringUtility {

    public static List<String> splitLines(String rawText) {
        if (rawText == null) {
            return null;
        }

        String[] rawLines = rawText.trim().split("\\R");

        List<String> lines = new ArrayList<>();

        for (String rawLine : rawLines) {
            String line = rawLine.trim();
            if (line.length() > 0) {
                lines.add(line);
            }
        }

        return lines;
    }

    public static String joinLines(List<String> lines) {
        if (lines == null) {
            return null;
        }

        if (lines.size() == 0) {
            return "";
        }

        StringJoiner stringJoiner = new StringJoiner("\n");

        for (String line : lines) {
            stringJoiner.add(line.trim());
        }

        return stringJoiner.toString();
    }
}
