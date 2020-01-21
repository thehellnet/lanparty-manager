package org.thehellnet.utility;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class ResourceUtility {

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

        return ResourceUtility.class.getClassLoader().getResourceAsStream(path);
    }

    public String getResourceContent() {
        if (path == null || path.length() == 0) {
            return null;
        }

        InputStream inputStream = ResourceUtility.class.getClassLoader().getResourceAsStream(path);
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
