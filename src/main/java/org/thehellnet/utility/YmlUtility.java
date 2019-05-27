package org.thehellnet.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public final class YmlUtility {

    private static final Logger logger = LoggerFactory.getLogger(YmlUtility.class);

    public static <T> T loadFromResources(String path, Class<T> type) {
        if (path == null || path.length() == 0) {
            return null;
        }

        InputStream inputStream = ResourceUtility.getResource(path);

        Yaml yaml = new Yaml();
        return yaml.loadAs(inputStream, type);
    }
}
