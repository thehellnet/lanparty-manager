package org.thehellnet.utility;

import java.util.regex.Pattern;

public final class EmailUtility {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

    private final String email;

    private EmailUtility(String email) {
        this.email = email;
    }

    public static EmailUtility newInstance(String email) {
        return new EmailUtility(email);
    }

    public boolean validate() {
        if (checkNullOrEmpty()) {
            return false;
        }

        return matches();
    }

    public boolean validateForLogin() {
        if (checkNullOrEmpty()) {
            return false;
        }

        if (email.strip().equals("admin")) {
            return true;
        }

        return matches();
    }

    boolean checkNullOrEmpty() {
        if (email == null) {
            return true;
        }

        return email.strip().length() == 0;
    }

    private boolean matches() {
        return EMAIL_PATTERN.matcher(email.strip()).matches();
    }
}
