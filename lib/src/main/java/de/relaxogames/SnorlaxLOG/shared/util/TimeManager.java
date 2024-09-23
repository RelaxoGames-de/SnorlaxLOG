package de.relaxogames.SnorlaxLOG.shared.util;

import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * The TimeManager class provides utility methods for formatting Timestamps into
 * different String representations based on a specified time zone.
 */
public class TimeManager {
    /**
     * The timeZone variable holds the TimeZone instance that is used for formatting
     * timestamps into different string representations within the TimeManager class.
     * It can be set via the constructor or defaults to "Europe/Berlin".
     */
    private static TimeZone timeZone;

    /**
     * Constructs a TimeManager instance with the specified time zone.
     *
     * @param timeZone the TimeZone instance to be used for formatting timestamps
     */
    public TimeManager(TimeZone timeZone) {
        TimeManager.timeZone = timeZone;
    }

    /**
     * Constructs a TimeManager instance with the default time zone set to "Europe/Berlin".
     */
    public TimeManager() {
        TimeManager.timeZone = TimeZone.getTimeZone( "Europe/Berlin");
    }

    /**
     * Formats the given Timestamp into a String representation based on the specified time zone.
     *
     * @param time the Timestamp to be formatted
     * @return a formatted String representation of the given Timestamp
     */
    public String getFormattedFull(Timestamp time) {
        long until = time.getTime();
        Date date = new Date(until);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        format.setTimeZone(timeZone);
        return format.format(date);
    }

    /**
     * Formats the given Timestamp into a String representation of the time (hours and minutes)
     * based on the specified time zone.
     *
     * @param time the Timestamp to be formatted
     * @return a formatted String representation of the time from the given Timestamp
     */
    public String getFormattedTime(Timestamp time) {
        long until = time.getTime();
        Date date = new Date(until);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(timeZone);
        return format.format(date);
    }

    /**
     * Formats the given Timestamp into a String representation of the date
     * (day, month, and year) based on the specified time zone.
     *
     * @param time the Timestamp to be formatted
     * @return a formatted String representation of the date from the given Timestamp
     */
    public String getFormattedDate(Timestamp time) {
        long until = time.getTime();
        Date date = new Date(until);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        format.setTimeZone(timeZone);
        return format.format(date);
    }

    /**
     * Retrieves the current time zone.
     *
     * @return the currently configured TimeZone instance
     */
    public static TimeZone getTimeZone() {
        return timeZone;
    }
}
