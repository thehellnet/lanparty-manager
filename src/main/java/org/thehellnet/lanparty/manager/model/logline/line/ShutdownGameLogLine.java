package org.thehellnet.lanparty.manager.model.logline.line;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.logline.LineEvent;

public class ShutdownGameLogLine extends LogLine {

    public ShutdownGameLogLine(DateTime dateTime, int uptime, LineEvent lineEvent) {
        super(dateTime, uptime, lineEvent);
    }
}
