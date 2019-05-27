package org.thehellnet.utility;

import java.io.InputStream;

public final class ResourceUtility {

    public static InputStream getResource(String path) {
        if (path == null || path.length() == 0) {
            return null;
        }

        return ResourceUtility.class.getClassLoader().getResourceAsStream(path);
    }
}
