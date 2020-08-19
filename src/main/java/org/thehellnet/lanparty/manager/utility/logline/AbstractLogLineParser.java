package org.thehellnet.lanparty.manager.utility.logline;


import org.thehellnet.lanparty.manager.exception.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.logline.LogLineParserException;
import org.thehellnet.lanparty.manager.model.logline.LineEvent;
import org.thehellnet.lanparty.manager.model.logline.line.LogLine;
import org.thehellnet.lanparty.manager.model.logline.line.UnusefulLogLine;
import org.thehellnet.utility.StringUtility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public abstract class AbstractLogLineParser implements LogLineParser {

    private static final String LINE_EVENT_NOT_IMPLEMENTED = "Line Event not implemented";

    protected final String rawLogLine;

    protected LocalDateTime lineDateTime;
    protected int lineTime;

    public AbstractLogLineParser(String rawLogLine) {
        this.rawLogLine = rawLogLine;
    }

    @Override
    public LogLine parse(LocalDateTime dateTime) {
        validate(rawLogLine);

        List<String> mainItems = StringUtility.splitSpaces(rawLogLine);
        if (mainItems.size() < 2) {
            throw new LogLineParserException("Line not valid");
        }

        lineDateTime = dateTime;

        String time = mainItems.get(0);
        lineTime = parseTime(time);

        String data = StringUtility.joinSpaces(mainItems.subList(1, mainItems.size()));
        String[] items = splitItems(data);

        LineEvent lineEvent = parseEvent(items[0]);

        switch (lineEvent) {
            case INIT_GAME:
                return parseInitGame(items);
            case SHUTDOWN_GAME:
                return parseShutdownGame(items);
            case JOIN:
                return parseJoin(items);
            case QUIT:
                return parseQuit(items);
            case DAMAGE:
                return parseDamage(items);
            case KILL:
                return parseKill(items);
            case SAY:
                return parseSay(items);
            case WEAPON:
                return parseWeapon(items);
            case UNUSEFUL:
                return new UnusefulLogLine(lineDateTime, lineTime);
        }

        throw new InvalidDataException("Line tag not supported by parser");
    }

    protected abstract Map<String, LineEvent> getLineTags();

    protected LogLine parseInitGame(String[] items) {
        throw new LogLineParserException(LINE_EVENT_NOT_IMPLEMENTED);
    }

    protected LogLine parseShutdownGame(String[] items) {
        throw new LogLineParserException(LINE_EVENT_NOT_IMPLEMENTED);
    }

    protected LogLine parseJoin(String[] items) {
        throw new LogLineParserException(LINE_EVENT_NOT_IMPLEMENTED);
    }

    protected LogLine parseQuit(String[] items) {
        throw new LogLineParserException(LINE_EVENT_NOT_IMPLEMENTED);
    }

    protected LogLine parseDamage(String[] items) {
        throw new LogLineParserException(LINE_EVENT_NOT_IMPLEMENTED);
    }

    protected LogLine parseKill(String[] items) {
        throw new LogLineParserException(LINE_EVENT_NOT_IMPLEMENTED);
    }

    protected LogLine parseSay(String[] items) {
        throw new LogLineParserException(LINE_EVENT_NOT_IMPLEMENTED);
    }

    protected LogLine parseWeapon(String[] items) {
        throw new LogLineParserException(LINE_EVENT_NOT_IMPLEMENTED);
    }

    LineEvent parseEvent(String lineTag) {
        if (lineTag == null) {
            throw new InvalidDataException("Invalid Line tag");
        }

        Map<String, LineEvent> lineTags = getLineTags();
        for (Map.Entry<String, LineEvent> entry : lineTags.entrySet()) {
            String checkTag = entry.getKey();
            if (checkTag.equals(lineTag) || lineTag.startsWith(checkTag)) {
                return entry.getValue();
            }
        }

        throw new InvalidDataException("Line tag not supported by parser");
    }

    static void validate(String rawLogLine) {
        if (rawLogLine == null || rawLogLine.length() == 0) {
            throw new LogLineParserException("Line null or empty");
        }
    }

    static String[] splitItems(String data) {
        if (data == null || data.strip().length() == 0) {
            throw new LogLineParserException("Invalid data string");
        }
        return data.split(";", -1);
    }

    static int parseTime(String time) {
        if (time == null
                || time.length() == 0
                || !time.contains(":")) {
            throw new LogLineParserException("Time string not valid");
        }

        String[] items = time.strip().split(":");
        if (items.length != 2) {
            throw new LogLineParserException("Invalid items in time string");
        }

        String minutesString = items[0];
        String secondsString = items[1];

        if (minutesString.length() == 0 || secondsString.length() == 0) {
            throw new LogLineParserException("Empty items in time string");
        }

        int minutes = Integer.parseInt(minutesString);
        int seconds = Integer.parseInt(secondsString);

        return minutes * 60 + seconds;
    }
}
