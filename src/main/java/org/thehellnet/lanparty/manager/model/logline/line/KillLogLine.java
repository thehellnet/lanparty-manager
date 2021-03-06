package org.thehellnet.lanparty.manager.model.logline.line;


import org.thehellnet.lanparty.manager.model.logline.LineEvent;

import java.time.LocalDateTime;
import java.util.Objects;

@LineEventType(LineEvent.KILL)
public class KillLogLine extends LogLine {

    private String affectedGuid;
    private int affectedNum;
    private String affectedNick;

    private String offendingGuid;
    private int offendingNum;
    private String offendingNick;

    private String weapon;
    private int damage;
    private String bullet;
    private String zone;

    public KillLogLine(LocalDateTime dateTime, int uptime) {
        super(dateTime, uptime);
    }

    public String getAffectedGuid() {
        return affectedGuid;
    }

    public void setAffectedGuid(String affectedGuid) {
        this.affectedGuid = affectedGuid;
    }

    public int getAffectedNum() {
        return affectedNum;
    }

    public void setAffectedNum(int affectedNum) {
        this.affectedNum = affectedNum;
    }

    public String getAffectedNick() {
        return affectedNick;
    }

    public void setAffectedNick(String affectedNick) {
        this.affectedNick = affectedNick;
    }

    public String getOffendingGuid() {
        return offendingGuid;
    }

    public void setOffendingGuid(String offendingGuid) {
        this.offendingGuid = offendingGuid;
    }

    public int getOffendingNum() {
        return offendingNum;
    }

    public void setOffendingNum(int offendingNum) {
        this.offendingNum = offendingNum;
    }

    public String getOffendingNick() {
        return offendingNick;
    }

    public void setOffendingNick(String offendingNick) {
        this.offendingNick = offendingNick;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getBullet() {
        return bullet;
    }

    public void setBullet(String bullet) {
        this.bullet = bullet;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        KillLogLine that = (KillLogLine) o;
        return affectedNum == that.affectedNum &&
                offendingNum == that.offendingNum &&
                damage == that.damage &&
                Objects.equals(affectedGuid, that.affectedGuid) &&
                Objects.equals(affectedNick, that.affectedNick) &&
                Objects.equals(offendingGuid, that.offendingGuid) &&
                Objects.equals(offendingNick, that.offendingNick) &&
                Objects.equals(weapon, that.weapon) &&
                Objects.equals(bullet, that.bullet) &&
                Objects.equals(zone, that.zone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), affectedGuid, affectedNum, affectedNick, offendingGuid, offendingNum, offendingNick, weapon, damage, bullet, zone);
    }
}
