package org.thehellnet.utility;

import org.thehellnet.utility.exception.InvalidValueException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Q3AUtils {

    public static String removeColorCodes(String input) {
        return input.trim().replaceAll("(\\^[0-9])+", "");
    }

    public static String clearString(String input) {
        return input.trim().replaceAll("\\^([0-9])+$", "");
    }

    public static String removeDoubleQuotes(String input) {
        return input.replaceAll("(^\")|(\"$)", "");
    }

    public static String tagToColor(String message) {
        Pattern pattern = Pattern.compile("\\$\\{/?[a-z]+}");
        Matcher matcher = pattern.matcher(message.trim());
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String value = matcher.group().toLowerCase();
            switch (value) {
                case "${black}":
                    matcher.appendReplacement(stringBuffer, "^0");
                    break;
                case "${red}":
                    matcher.appendReplacement(stringBuffer, "^1");
                    break;
                case "${green}":
                    matcher.appendReplacement(stringBuffer, "^2");
                    break;
                case "${yellow}":
                    matcher.appendReplacement(stringBuffer, "^3");
                    break;
                case "${blue}":
                    matcher.appendReplacement(stringBuffer, "^4");
                    break;
                case "${cyan}":
                    matcher.appendReplacement(stringBuffer, "^5");
                    break;
                case "${pink}":
                    matcher.appendReplacement(stringBuffer, "^6");
                    break;
                case "${white}":
                    matcher.appendReplacement(stringBuffer, "^7");
                    break;
                default:
                    throw new InvalidValueException(String.format("Value not valid: %s", value));
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static String colorToTag(String message) {
        Pattern pattern = Pattern.compile("\\^[0-9]+");
        Matcher matcher = pattern.matcher(message.trim());
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            switch (matcher.group().toLowerCase()) {
                case "^0":
                    matcher.appendReplacement(stringBuffer, "\\${black}");
                    break;
                case "^1":
                    matcher.appendReplacement(stringBuffer, "\\${red}");
                    break;
                case "^2":
                    matcher.appendReplacement(stringBuffer, "\\${green}");
                    break;
                case "^3":
                    matcher.appendReplacement(stringBuffer, "\\${yellow}");
                    break;
                case "^4":
                    matcher.appendReplacement(stringBuffer, "\\${blue}");
                    break;
                case "^5":
                    matcher.appendReplacement(stringBuffer, "\\${cyan}");
                    break;
                case "^6":
                    matcher.appendReplacement(stringBuffer, "\\${pink}");
                    break;
                case "^7":
                    matcher.appendReplacement(stringBuffer, "\\${white}");
                    break;
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static int pingToInteger(String ping) {
        if (ping == null) return -1;
        if (ping.equals("ZMBI")) return -1;
        if (ping.equals("999")) return -1;
        try {
            return Integer.parseInt(ping);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static String cleanIpAddress(String ipAddress) {
        return ipAddress.split(":")[0];
    }
}
