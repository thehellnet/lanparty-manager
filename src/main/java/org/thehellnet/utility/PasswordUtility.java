package org.thehellnet.utility;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PasswordUtility {

    private static final int ARGON2_ITERATIONS = 65536;
    private static final int ARGON2_MEMORY_COST = 2;
    private static final int ARGON2_PARALLELISM = 1;

    private final Argon2 argon2;

    private PasswordUtility() {
        argon2 = Argon2Factory.create();
    }

    public static PasswordUtility newInstance() {
        return new PasswordUtility();
    }

    public String hash(String password) {
        if (password == null || password.length() == 0) {
            return null;
        }

        char[] passwd = password.toCharArray();

        String hash = argon2.hash(ARGON2_MEMORY_COST, ARGON2_ITERATIONS, ARGON2_PARALLELISM, passwd);
        argon2.wipeArray(passwd);

        return hash;
    }

    public boolean verify(String hash, String password) {
        if (hash == null || hash.length() == 0
                || password == null || password.length() == 0) {
            return false;
        }

        return argon2.verify(hash, password.toCharArray());
    }
}
