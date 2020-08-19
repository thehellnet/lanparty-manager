package org.thehellnet.lanparty.manager.model.logline.line;


import org.thehellnet.lanparty.manager.model.logline.LineEvent;

import java.time.LocalDateTime;
import java.util.Objects;

@LineEventType(LineEvent.QUIT)
public class QuitLogLine extends LogLine {

    private String guid;
    private int num;
    private String nick;

    public QuitLogLine(LocalDateTime dateTime, int uptime) {
        super(dateTime, uptime);
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuitLogLine that = (QuitLogLine) o;
        return num == that.num &&
                guid.equals(that.guid) &&
                nick.equals(that.nick);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), guid, num, nick);
    }
}
