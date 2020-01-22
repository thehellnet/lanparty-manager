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

    private final String path;

    private ResourceUtility(String path) {
        this.path = path;
    }

    public static ResourceUtility getInstance(String path) {
        return new ResourceUtility(path);
    }

    public InputStream getResource() {
        if (path == null || path.length() == 0) {
            return null;
        }

        InputStream inputStream = null;

        for (String searchPath : LOCAL_RESOURCES_PATH) {
            String filePath = PathUtility.join(searchPath, path);
            File file = new File(filePath);

            if (file.exists() && file.canRead()) {
                FileInputStream fileInputStream;

                try {
                    fileInputStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    logger.warn("File found but unable to get InputStream");
                    continue;
                }

                inputStream = fileInputStream;
            }
        }

        if (inputStream == null) {
            inputStream = ResourceUtility.class.getClassLoader().getResourceAsStream(path);
        }

        return inputStream;
    }

    public String getResourceContent() {
        if (path == null || path.length() == 0) {
            return null;
        }

        InputStream inputStream = getResource();
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
