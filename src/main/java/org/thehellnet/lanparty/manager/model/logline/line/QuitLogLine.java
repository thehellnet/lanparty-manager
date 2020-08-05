package org.thehellnet.lanparty.manager.model.logline.line;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.logline.LineEvent;

public class QuitLogLine extends JoinLogLine {

    public QuitLogLine(DateTime dateTime, int uptime, LineEvent lineEvent) {
        super(dateTime, uptime, lineEvent);
    }
}
