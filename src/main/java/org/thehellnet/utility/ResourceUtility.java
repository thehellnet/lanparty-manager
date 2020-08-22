package org.thehellnet.utility;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class ResourceUtility {

    private static final String[] LOCAL_RESOURCES_PATH = new String[]{
            PathUtility.join("/opt", "lanparty", "manager"),
            PathUtility.join(System.getProperty("user.home"), "lanparty", "manager")
    };

    private static final Logger logger = LoggerFactory.getLogger(ResourceUtility.class);

    private ResourceUtility() {
    }

    public static InputStream getResource(String path) {
        return getResource(path, false);
    }

    public static InputStream getResource(String path, boolean internalOnly) {
        if (path == null || path.length() == 0) {
            return null;
        }

        InputStream inputStream = null;

        if (!internalOnly) {
            File file = null;

            for (String searchPath : LOCAL_RESOURCES_PATH) {
                String filePath = PathUtility.join(searchPath, path);
                file = new File(filePath);
                if (file.exists() || file.canRead()) {
                    break;
                }
            }

            if (file != null) {
                try {
                    inputStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    logger.warn("File found but unable to get InputStream");
                }
            }
        }

        if (inputStream == null) {
            inputStream = ResourceUtility.class.getClassLoader().getResourceAsStream(path);
        }

        return inputStream;
    }

    public static String getResourceContent(String path) {
        return getResourceContent(path, false);
    }

    public static String getResourceContent(String path, boolean internalOnly) {
        if (path == null || path.length() == 0) {
            return null;
        }

        InputStream inputStream = getResource(path, internalOnly);
        if (inputStream == null) {
            logger.error("Resource {} not found", path);
            return null;
        }

        String content;

        try {
            content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Unable to retrieve resource: {}", e.getMessage());
            return null;
        }

        return content;
    }
}
