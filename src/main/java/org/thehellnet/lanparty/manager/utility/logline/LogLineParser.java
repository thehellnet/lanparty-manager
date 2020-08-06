package org.thehellnet.lanparty.manager.utility.logline;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.logline.line.LogLine;

public interface LogLineParser {

    LogLine parse(DateTime dateTime);
}
