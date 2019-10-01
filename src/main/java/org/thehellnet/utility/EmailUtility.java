package org.thehellnet.utility;

import java.util.regex.Pattern;

public final class EmailUtility {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

    public static boolean validate(String email) {
        if (checkNullOrEmpty(email)) {
            return false;
        }

        return matches(email);
    }

    public static boolean validateForLogin(String email) {
        if (checkNullOrEmpty(email)) {
            return false;
        }

        if (email.strip().equals("admin")) {
            return true;
        }

        return matches(email);
    }

    static boolean checkNullOrEmpty(String email) {
        if (email == null) {
            return true;
        }

        return email.strip().length() == 0;
    }

    private static boolean matches(String email) {
        return EMAIL_PATTERN.matcher(email.strip()).matches();
    }
}
