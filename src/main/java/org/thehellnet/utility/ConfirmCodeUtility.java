package org.thehellnet.utility;

public final class ConfirmCodeUtility {

    public static final int LENGTH = 64;
    private static final int SEED = 2048;
    private static final int OFFSET = 17;

    private ConfirmCodeUtility() {
    }

    public static String generate() {
        return StringUtility.randomString(SEED, OFFSET, LENGTH);
    }
}
