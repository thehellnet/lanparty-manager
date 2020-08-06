package org.thehellnet.lanparty.manager.model.logline.line;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.logline.LineEvent;

@LineEventType(LineEvent.SHUTDOWN_GAME)
public class ShutdownGameLogLine extends LogLine {

    public ShutdownGameLogLine(DateTime dateTime, int uptime) {
        super(dateTime, uptime);
    }
}
