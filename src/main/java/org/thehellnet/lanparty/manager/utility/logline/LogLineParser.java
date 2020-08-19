package org.thehellnet.lanparty.manager.utility.logline;


import org.thehellnet.lanparty.manager.model.logline.line.LogLine;

import java.time.LocalDateTime;

public interface LogLineParser {

    LogLine parse(LocalDateTime dateTime);
}
