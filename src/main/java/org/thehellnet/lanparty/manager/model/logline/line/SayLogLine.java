package org.thehellnet.lanparty.manager.model.logline.line;


import org.thehellnet.lanparty.manager.model.logline.LineEvent;

import java.time.LocalDateTime;
import java.util.Objects;

@LineEventType(LineEvent.SAY)
public class SayLogLine extends LogLine {

    private String guid;
    private int num;
    private String nick;
    private String message;

    public SayLogLine(LocalDateTime dateTime, int uptime) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SayLogLine that = (SayLogLine) o;
        return num == that.num &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(nick, that.nick) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), guid, num, nick, message);
    }
}
