package org.thehellnet.utility;

import org.thehellnet.utility.exception.InvalidPathException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public final class YmlUtility<T> {

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
        return loadFromResources(false);
    }

    public T loadFromResources(boolean internalOnly) {
        if (path == null || path.length() == 0) {
            throw new InvalidPathException(String.format("Path \"%s\" not valid", path));
        }

        InputStream inputStream = ResourceUtility.getInstance(path).getResource(internalOnly);

        Yaml yaml = new Yaml();
        return yaml.loadAs(inputStream, type);
    }
}
