package org.thehellnet.lanparty.manager.model.logline.line;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.logline.LineEvent;

@LineEventType(LineEvent.QUIT)
public class QuitLogLine extends JoinLogLine {

    public QuitLogLine(DateTime dateTime, int uptime) {
        super(dateTime, uptime);
    }
}
