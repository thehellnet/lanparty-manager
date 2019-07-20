package org.thehellnet.lanparty.manager.model;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.constraints.NotNull;
import java.util.Map;

public interface CustomJSONSerializable {

    @JsonValue
    @NotNull
    Map<String, Object> toJSON();
}
