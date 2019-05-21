package org.thehellnet.utility;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PasswordUtility {

    private static final int ARGON2_ITERATIONS = 65536;
    private static final int ARGON2_MEMORY_COST = 2;
    private static final int ARGON2_PARALLELISM = 1;

    private static final Logger logger = LoggerFactory.getLogger(PasswordUtility.class);

    public static String hash(String password) {
        if (password == null) {
            return null;
        }

        char[] passwd = password.toCharArray();

        Argon2 argon2 = Argon2Factory.create();
        String hash = argon2.hash(ARGON2_MEMORY_COST, ARGON2_ITERATIONS, ARGON2_PARALLELISM, passwd);
        argon2.wipeArray(passwd);

        return hash;
    }

    public static boolean verify(String hash, String password) {
        return Argon2Factory.create().verify(hash, password);
    }
}
