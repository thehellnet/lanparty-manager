package org.thehellnet.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class StringUtility {

    public static List<String> splitLines(String rawText) {
        if (rawText == null) {
            return null;
        }

        String[] rawLines = rawText.trim().split("\n");

        List<String> lines = new ArrayList<>();

        for (String rawLine : rawLines) {
            String line = rawLine.trim();
            if (line.length() > 0) {
                lines.add(line);
            }
        }

        return lines;
    }
}
