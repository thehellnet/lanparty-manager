package org.thehellnet.lanparty.manager.utility.logline.line;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.utility.logline.LineEvent;

import java.util.Objects;

public abstract class LogLine {

    protected final DateTime dateTime;
    protected final int uptime;
    protected final LineEvent lineEvent;

    public LogLine(DateTime dateTime, int uptime, LineEvent lineEvent) {
        this.dateTime = dateTime;
        this.uptime = uptime;
        this.lineEvent = lineEvent;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public int getUptime() {
        return uptime;
    }

    public LineEvent getLineEvent() {
        return lineEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogLine logLine = (LogLine) o;
        return dateTime.equals(logLine.dateTime) &&
                uptime == logLine.uptime &&
                lineEvent == logLine.lineEvent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, uptime, lineEvent);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", dateTime, lineEvent);
    }
}
