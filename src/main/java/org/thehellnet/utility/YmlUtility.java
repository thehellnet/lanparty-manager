package org.thehellnet.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public final class YmlUtility<T> {

    private static final Logger logger = LoggerFactory.getLogger(YmlUtility.class);

    private final String path;
    private final Class<T> type;

    private YmlUtility(String path, Class<T> type) {
        this.path = path;
        this.type = type;
    }

    public static <T> YmlUtility<T> getInstance(String path, Class<T> type) {
        return new YmlUtility<T>(path, type);
    }

    public T loadFromResources() {
        if (path == null || path.length() == 0) {
            return null;
        }

        InputStream inputStream = ResourceUtility.getInstance(path).getResource();

        Yaml yaml = new Yaml();
        return yaml.loadAs(inputStream, type);
    }
}
