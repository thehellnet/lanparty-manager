package org.thehellnet.lanparty.manager.model.logline.line;


import org.thehellnet.lanparty.manager.model.logline.LineEvent;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@LineEventType(LineEvent.INIT_GAME)
public class InitGameLogLine extends LogLine {

    private final Map<String, Object> params = new HashMap<>();

    private String gametypeTag;
    private String mapTag;

    public InitGameLogLine(LocalDateTime dateTime, int uptime) {
        super(dateTime, uptime);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getGametypeTag() {
        return gametypeTag;
    }

    public void setGametypeTag(String gametypeTag) {
        this.gametypeTag = gametypeTag;
    }

    public String getMapTag() {
        return mapTag;
    }

    public void setMapTag(String mapTag) {
        this.mapTag = mapTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InitGameLogLine that = (InitGameLogLine) o;
        return params.equals(that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), params);
    }
}
