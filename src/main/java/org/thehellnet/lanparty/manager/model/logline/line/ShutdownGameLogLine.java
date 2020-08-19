package org.thehellnet.lanparty.manager.model.logline.line;


import org.thehellnet.lanparty.manager.model.logline.LineEvent;

import java.time.LocalDateTime;

@LineEventType(LineEvent.SHUTDOWN_GAME)
public class ShutdownGameLogLine extends LogLine {

    public ShutdownGameLogLine(LocalDateTime dateTime, int uptime) {
        super(dateTime, uptime);
    }
}
