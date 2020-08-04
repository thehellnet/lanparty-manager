package org.thehellnet.utility;

import org.joda.time.DateTime;

public final class TokenUtility {

    public static final int LENGTH = 16;

    private static final int SEED = 1024;
    private static final int OFFSET = 19;
    private static final int EXIPRATION_DAYS = 1;

    private TokenUtility() {
    }

    public static String generate() {
        return StringUtility.randomString(SEED, OFFSET, LENGTH);
    }

    public static DateTime generateExpiration(DateTime creationDateTime) {
        return creationDateTime.plusDays(EXIPRATION_DAYS);
    }

    public static boolean validateExpiration(DateTime expirationDateTime) {
        return expirationDateTime.isAfterNow();
    }
}
