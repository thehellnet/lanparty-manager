package org.thehellnet.utility;

public final class EmailUtility {

    public static boolean validate(String email) {
        if (checkNullOrEmpty(email)) {
            return false;
        }

        return email.contains("@");
    }

    public static boolean validateForLogin(String email) {
        if (checkNullOrEmpty(email)) {
            return false;
        }

        if (email.equals("admin")) {
            return true;
        }

        return email.contains("@");
    }

    private static boolean checkNullOrEmpty(String email) {
        if (email == null) {
            return true;
        }

        return email.length() == 0;
    }
}
