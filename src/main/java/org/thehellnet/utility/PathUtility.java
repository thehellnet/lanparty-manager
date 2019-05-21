package org.thehellnet.utility;

import java.io.File;

public final class PathUtility {

    public static String join(String... items) {
        String finalPath = "";

        switch (items.length) {
            case 0:
                break;
            case 1:
                finalPath = items[0];
                break;
            default:
                finalPath = new File(items[0]).toString();
                for (int i = 1; i < items.length; i++) {
                    String item = items[i];
                    finalPath = new File(finalPath, item).toString();
                }
        }

        return finalPath;
    }
}