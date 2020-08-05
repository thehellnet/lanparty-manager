package org.thehellnet.lanparty.manager.model.logline;

import org.thehellnet.lanparty.manager.exception.logline.LogLineParserException;
import org.thehellnet.lanparty.manager.model.logline.line.LogLine;

public abstract class AbstractLogLineParser implements LogLineParser {

    protected final String line;

    public AbstractLogLineParser(String line) {
        this.line = line;
    }

    public LogLine parse() {
        validate();

        return parseLine();
    }

    protected abstract LogLine parseLine();

    protected void validate() {
        if (line == null || line.length() == 0) {
            throw new LogLineParserException("Line null or empty");
        }
    }

    protected static int parseTime(String time) {
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

    protected static String[] splitItems(String data) {
        if (data == null || data.strip().length() == 0) {
            throw new LogLineParserException("Invalid data string");
        }
        return data.split(";", -1);
    }
}
