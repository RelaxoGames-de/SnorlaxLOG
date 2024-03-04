package de.snorlaxlog.shared.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeManager {

    public static String getFormattedTime(Timestamp time){
        long current = System.currentTimeMillis();
        long until = time.getTime() + 3600000L;

        TimeZone timezone = TimeZone.getTimeZone("Germany/Berlin"); // example timezone

        Date date = new Date(until);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        formatter.setTimeZone(timezone);
        return formatter.format(date);
    }

    public static String getFormattedHours(Timestamp time){
        long current = System.currentTimeMillis();
        long until = time.getTime() + 3600000L;

        TimeZone timezone = TimeZone.getTimeZone("Germany/Berlin"); // example timezone

        Date date = new Date(until);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(timezone);
        return formatter.format(date);
    }

    public static String getFormattedYears(Timestamp time){
        long current = System.currentTimeMillis();
        long until = time.getTime() + 3600000L;

        TimeZone timezone = TimeZone.getTimeZone("Germany/Berlin"); // example timezone

        Date date = new Date(until);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        formatter.setTimeZone(timezone);
        return formatter.format(date);
    }

    public static String getFormattedTimeLore(Timestamp time){
        long current = System.currentTimeMillis();
        long until = time.getTime() + 3600000L;

        TimeZone timezone = TimeZone.getTimeZone("Germany/Berlin"); // example timezone

        Date date = new Date(until);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM HH:mm");
        formatter.setTimeZone(timezone);
        return formatter.format(date);
    }
}
