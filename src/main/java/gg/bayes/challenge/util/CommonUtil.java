package gg.bayes.challenge.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CommonUtil {
    public static LocalTime extractTimeInMilliseconds(String sValue) {
        String[] split = sValue.split("]");
        return LocalTime.parse(split[0], DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
    }

    private CommonUtil() {
        // Private constructor to stop initialization
    }
}
