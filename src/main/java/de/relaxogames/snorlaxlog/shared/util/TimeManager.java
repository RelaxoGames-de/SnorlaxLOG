package de.relaxogames.snorlaxlog.shared.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeManager {

    /**
     * Returns a formatted string representation of the provided Timestamp 'time' in
     * the "dd.MM.yyyy HH:mm" format,
     * adjusted to the "Germany/Berlin" timezone.
     *
     * @param time the Timestamp to be formatted
     * @return the formatted string representation of the Timestamp
     */
    public static String getFormattedTime(Timestamp time) {
        @SuppressWarnings("unused")
        long current = System.currentTimeMillis();
        long until = time.getTime() + 3600000L;
        TimeZone timezone = TimeZone.getTimeZone("Germany/Berlin");
        Date date = new Date(until);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        formatter.setTimeZone(timezone);
        return formatter.format(date);
    }

    /**
     * Returns a formatted string representation of the provided Timestamp 'time' in
     * the "dd.MM.yyyy HH:mm" format,
     * adjusted to the "Germany/Berlin" timezone.
     *
     * @param time the Timestamp to be formatted
     * @return the formatted string representation of the Timestamp
     */
    public static String getFormattedHours(Timestamp time) {
        @SuppressWarnings("unused")
        long current = System.currentTimeMillis();
        long until = time.getTime() + 3600000L;
        TimeZone timezone = TimeZone.getTimeZone("Germany/Berlin");
        Date date = new Date(until);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(timezone);
        return formatter.format(date);
    }

    /**
     * Returns a formatted string representation of the provided Timestamp 'time' in
     * the "dd.MM.yyyy" format,
     * adjusted to the "Germany/Berlin" timezone.
     *
     * @param time the Timestamp to be formatted
     * @return the formatted string representation of the Timestamp
     */
    public static String getFormattedYears(Timestamp time) {
        @SuppressWarnings("unused")
        long current = System.currentTimeMillis();
        long until = time.getTime() + 3600000L;
        TimeZone timezone = TimeZone.getTimeZone("Germany/Berlin");
        Date date = new Date(until);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        formatter.setTimeZone(timezone);
        return formatter.format(date);
    }

    /**
     * Returns a formatted string representation of the provided Timestamp 'time' in
     * the "dd.MM HH:mm" format,
     * adjusted to the "Germany/Berlin" timezone.
     *
     * @param time the Timestamp to be formatted
     * @return the formatted string representation of the Timestamp
     */
    public static String getFormattedTimeLore(Timestamp time) {
        @SuppressWarnings("unused")
        long current = System.currentTimeMillis();
        long until = time.getTime() + 3600000L;
        TimeZone timezone = TimeZone.getTimeZone("Germany/Berlin");
        Date date = new Date(until);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM HH:mm");
        formatter.setTimeZone(timezone);
        return formatter.format(date);
    }
}
