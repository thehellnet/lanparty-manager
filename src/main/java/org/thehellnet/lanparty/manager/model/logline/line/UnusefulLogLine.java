package org.thehellnet.lanparty.manager.model.logline.line;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.logline.LineEvent;

@LineEventType(LineEvent.UNUSEFUL)
public class UnusefulLogLine extends LogLine {

    public UnusefulLogLine(DateTime dateTime, int uptime) {
        super(dateTime, uptime);
    }
}
