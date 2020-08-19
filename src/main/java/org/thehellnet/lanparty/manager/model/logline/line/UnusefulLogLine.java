package org.thehellnet.lanparty.manager.model.logline.line;


import org.thehellnet.lanparty.manager.model.logline.LineEvent;

import java.time.LocalDateTime;

@LineEventType(LineEvent.UNUSEFUL)
public class UnusefulLogLine extends LogLine {

    public UnusefulLogLine(LocalDateTime dateTime, int uptime) {
        super(dateTime, uptime);
    }
}
