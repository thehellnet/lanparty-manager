package org.thehellnet.lanparty.manager.model.logline.line;


import org.thehellnet.lanparty.manager.model.logline.LineEvent;

import java.time.LocalDateTime;
import java.util.Objects;

@LineEventType(LineEvent.WEAPON)
public class WeaponLogLine extends LogLine {

    private String guid;
    private int num;
    private String nick;
    private String weapon;

    public WeaponLogLine(LocalDateTime dateTime, int uptime) {
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

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WeaponLogLine that = (WeaponLogLine) o;
        return num == that.num &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(nick, that.nick) &&
                Objects.equals(weapon, that.weapon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), guid, num, nick, weapon);
    }
}
