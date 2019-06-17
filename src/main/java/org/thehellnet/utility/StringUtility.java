package org.thehellnet.utility;

import java.util.Arrays;
import java.util.List;

public final class StringUtility {

    public static List<String> splitLines(String rawText) {
        String[] lines = rawText.trim().split("\n");
        return Arrays.asList(lines);
    }
}
