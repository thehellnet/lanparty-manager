package org.thehellnet.lanparty.manager.model.logline.line;


import java.time.LocalDateTime;
import java.util.Objects;

public abstract class LogLine {

    protected final LocalDateTime dateTime;
    protected final int uptime;

    public LogLine(LocalDateTime dateTime, int uptime) {
        this.dateTime = dateTime;
        this.uptime = uptime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getUptime() {
        return uptime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogLine logLine = (LogLine) o;
        return dateTime.equals(logLine.dateTime) &&
                uptime == logLine.uptime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, uptime);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
