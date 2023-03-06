package gg.bayes.challenge.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.MILLIS;

public class CommonUtil {

    /**
     * Method to calculate the time difference between the game start time and event occurred time
     *
     * @param sValue Raw event time extracted from the log
     * @return Time difference in milliseconds
     */
    public static long extractTimeDiffInMilliseconds(String sValue) {
        String[] splitTime = sValue.split("]");
        LocalTime eventTime = LocalTime.parse(splitTime[0], DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        return MILLIS.between(LocalTime.MIN, eventTime);
    }

    private CommonUtil() {
        // Private constructor to stop initialization
    }
}
