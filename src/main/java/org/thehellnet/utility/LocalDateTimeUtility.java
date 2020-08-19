package org.thehellnet.utility;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public final class LocalDateTimeUtility {

    private LocalDateTimeUtility() {
    }

    public static long getMillis(LocalDateTime localDateTime) {
        Clock systemClock = Clock.systemDefaultZone();
        return localDateTime.toInstant(ZoneOffset.of(systemClock.getZone().getId())).toEpochMilli();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return new Date(getMillis(localDateTime));
    }
}
