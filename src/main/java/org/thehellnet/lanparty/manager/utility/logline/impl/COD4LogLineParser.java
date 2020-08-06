package org.thehellnet.lanparty.manager.utility.logline.impl;

import org.thehellnet.lanparty.manager.model.constant.Tags;
import org.thehellnet.lanparty.manager.model.logline.LineEvent;
import org.thehellnet.lanparty.manager.model.logline.line.*;
import org.thehellnet.lanparty.manager.utility.logline.AbstractLogLineParser;
import org.thehellnet.lanparty.manager.utility.logline.LineParserGame;

import java.util.HashMap;
import java.util.Map;

@LineParserGame(gameTag = Tags.GAME_COD4)
public class COD4LogLineParser extends AbstractLogLineParser {

    public COD4LogLineParser(String rawLogLine) {
        super(rawLogLine);
    }

    @Override
    protected Map<String, LineEvent> getLineTags() {
        HashMap<String, LineEvent> lineEventHashMap = new HashMap<>();
        lineEventHashMap.put("InitGame", LineEvent.INIT_GAME);
        lineEventHashMap.put("ShutdownGame", LineEvent.SHUTDOWN_GAME);
        lineEventHashMap.put("J", LineEvent.JOIN);
        lineEventHashMap.put("Q", LineEvent.QUIT);
        lineEventHashMap.put("D", LineEvent.DAMAGE);
        lineEventHashMap.put("K", LineEvent.KILL);
        lineEventHashMap.put("W", LineEvent.WEAPON);
        lineEventHashMap.put("say", LineEvent.SAY);
        return lineEventHashMap;
    }

    @Override
    protected LogLine parseInitGame(String[] items) {
        InitGameLogLine logLine = new InitGameLogLine(lineDateTime, lineTime);

        String[] elems = items[0].split("\\\\");

        for (int i = 1; i < elems.length; i += 2) {
            String key = elems[i];
            String value = elems[i + 1];
            logLine.getParams().put(key, value);
        }

        logLine.setGametypeTag((String) logLine.getParams().getOrDefault("g_gametype", null));
        logLine.setMapTag((String) logLine.getParams().getOrDefault("mapname", null));

        return logLine;
    }

    @Override
    protected LogLine parseShutdownGame(String[] items) {
        return new ShutdownGameLogLine(lineDateTime, lineTime);
    }

    @Override
    protected LogLine parseJoin(String[] items) {
        JoinLogLine logLine = new JoinLogLine(lineDateTime, lineTime);
        logLine.setGuid(items[1]);
        logLine.setNum(Integer.parseInt(items[2]));
        logLine.setNick(items[3]);
        return logLine;
    }

    @Override
    protected LogLine parseQuit(String[] items) {
        QuitLogLine logLine = new QuitLogLine(lineDateTime, lineTime);
        logLine.setGuid(items[1]);
        logLine.setNum(Integer.parseInt(items[2]));
        logLine.setNick(items[3]);
        return logLine;
    }

    @Override
    protected LogLine parseDamage(String[] items) {
        DamageLogLine logLine = new DamageLogLine(lineDateTime, lineTime);
        logLine.setAffectedGuid(items[1]);
        logLine.setAffectedNum(Integer.parseInt(items[2]));
        logLine.setAffectedTeam(items[3]);
        logLine.setAffectedNick(items[4]);
        logLine.setOffendingGuid(items[5]);
        logLine.setOffendingNum(Integer.parseInt(items[6]));
        logLine.setOffendingTeam(items[7]);
        logLine.setOffendingNick(items[8]);
        logLine.setWeapon(items[9]);
        logLine.setDamage(Integer.parseInt(items[10]));
        logLine.setBullet(items[11]);
        logLine.setZone(items[12]);
        return logLine;
    }

    @Override
    protected LogLine parseKill(String[] items) {
        KillLogLine logLine = new KillLogLine(lineDateTime, lineTime);
        logLine.setAffectedGuid(items[1]);
        logLine.setAffectedNum(Integer.parseInt(items[2]));
        logLine.setAffectedNick(items[4]);
        logLine.setOffendingGuid(items[5]);
        logLine.setOffendingNum(Integer.parseInt(items[6]));
        logLine.setOffendingNick(items[8]);
        logLine.setWeapon(items[9]);
        logLine.setDamage(Integer.parseInt(items[10]));
        logLine.setBullet(items[11]);
        logLine.setZone(items[12]);
        return logLine;
    }

    @Override
    protected LogLine parseSay(String[] items) {
        SayLogLine logLine = new SayLogLine(lineDateTime, lineTime);
        logLine.setGuid(items[1]);
        logLine.setNum(Integer.parseInt(items[2]));
        logLine.setNick(items[3]);
        logLine.setMessage(items[4]);
        return logLine;
    }

    @Override
    protected LogLine parseWeapon(String[] items) {
        WeaponLogLine logLine = new WeaponLogLine(lineDateTime, lineTime);
        logLine.setGuid(items[1]);
        logLine.setNum(Integer.parseInt(items[2]));
        logLine.setNick(items[3]);
        logLine.setWeapon(items[4]);
        return logLine;
    }
}
