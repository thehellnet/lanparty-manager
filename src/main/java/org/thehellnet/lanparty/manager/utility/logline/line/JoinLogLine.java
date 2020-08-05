package org.thehellnet.lanparty.manager.utility.logline.line;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.utility.logline.LineEvent;

import java.util.Objects;

public class JoinLogLine extends LogLine {

    private String guid;
    private int num;
    private String nick;

    public JoinLogLine(DateTime dateTime, int uptime, LineEvent lineEvent) {
        super(dateTime, uptime, lineEvent);
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
        JoinLogLine that = (JoinLogLine) o;
        return num == that.num &&
                guid.equals(that.guid) &&
                nick.equals(that.nick);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), guid, num, nick);
    }
}
