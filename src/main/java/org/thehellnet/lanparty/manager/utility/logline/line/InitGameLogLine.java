package org.thehellnet.lanparty.manager.utility.logline.line;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.utility.logline.LineEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InitGameLogLine extends LogLine {

    private final Map<String, Object> params = new HashMap<>();

    public InitGameLogLine(DateTime dateTime, int uptime, LineEvent lineEvent) {
        super(dateTime, uptime, lineEvent);
    }

    public Map<String, Object> getParams() {
        return params;
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
