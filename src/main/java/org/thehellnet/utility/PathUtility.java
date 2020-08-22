package org.thehellnet.utility;

import java.io.File;

public final class PathUtility {

    private PathUtility() {
    }

    public static String join(String... items) {
        if (items == null
                || items.length == 0) {
            return "";
        }

        String finalPath = new File(items[0].strip()).toString();
        for (int i = 1; i < items.length; i++) {
            String item = items[i].strip();
            finalPath = new File(finalPath, item).toString();
        }

        return finalPath;
    }

    public static String absolute(File directory, String file) {
        return new File(directory, file).getAbsolutePath();
    }

    public static String absolute(String directory, String file) {
        return new File(directory, file).getAbsolutePath();
    }
}
