package cz.wake.corgibot.utils;

public class TimeUtils {

    private TimeUtils() {
    }

    private static void appendTimeAndUnit(StringBuffer timeBuf, long time, String unit) {
        if (time < 1) {
            return;
        }

        timeBuf.append(time);
        timeBuf.append(unit);
    }

    private static void prependTimeAndUnit(StringBuffer timeBuf, long time, String unit) {
        if (time < 1) {
            return;
        }

        if (timeBuf.length() > 0) {
            timeBuf.insert(0, " ");
        }

        timeBuf.insert(0, unit);
        timeBuf.insert(0, time);
    }

    /**
     * Provide the Millisecond time value in {year}y {day}d {hour}h {minute}m {second}s {millisecond}ms. <br>
     * Omitted if there is no value for that unit.
     *
     * @param timeInMillis
     * @return
     *
     * @since 2018. 1. 9.
     */
    public static String toYYYYHHmmssS(long timeInMillis) {

        if (timeInMillis < 1) {
            return "0 ms";
        }

        StringBuffer timeBuf = new StringBuffer();

        long millis = timeInMillis % 1000;
        //appendTimeAndUnit(timeBuf, millis, "ms");

        // second (1000ms) & above
        long time = timeInMillis / 1000;
        if (time < 1) {
            return timeBuf.toString();
        }

        long seconds = time % 60;
        //prependTimeAndUnit(timeBuf, seconds, "vteřin");

        // minute(60s) & above
        time = time / 60;
        if (time < 1) {
            return timeBuf.toString();
        }

        long minutes = time % 60;
        String minutesFormat = " minut";
        if(minutes == 1){
            minutesFormat = " minutu";
        } else if (minutes > 1 && minutes < 5){
            minutesFormat = " minuty";
        }
        prependTimeAndUnit(timeBuf, minutes, minutesFormat);

        // hour(60m) & above
        time = time / 60;
        if (time < 1) {
            return timeBuf.toString();
        }

        long hours = time % 24;
        String hourFormat = " hodin";
        if (hours == 1){
            hourFormat = " hodinu";
        } else if (hours > 1 && hours < 5){
            hourFormat = " hodiny";
        }
        prependTimeAndUnit(timeBuf, hours, hourFormat);

        // day(24h) & above
        time = time / 24;
        if (time < 1) {
            return timeBuf.toString();
        }

        long day = time % 365;
        String dayFormat = " dní";
        if(day == 1){
            dayFormat = " den";
        } else if (day > 1 && day < 5){
            dayFormat = " dny";
        }
        prependTimeAndUnit(timeBuf, day, dayFormat);

        // year(365d) ...
        time = time / 365;
        if (time < 1) {
            return timeBuf.toString();
        }

        prependTimeAndUnit(timeBuf, time, "y");

        return timeBuf.toString();
    }

    public static String toShortTime(long timeInMillis) {

        if (timeInMillis < 1) {
            return "0 ms";
        }

        StringBuffer timeBuf = new StringBuffer();

        long millis = timeInMillis % 1000;
        //appendTimeAndUnit(timeBuf, millis, "ms");

        // second (1000ms) & above
        long time = timeInMillis / 1000;
        if (time < 1) {
            return timeBuf.toString();
        }

        long seconds = time % 60;
        //prependTimeAndUnit(timeBuf, seconds, "vteřin");

        // minute(60s) & above
        time = time / 60;
        if (time < 1) {
            return timeBuf.toString();
        }

        long minutes = time % 60;
        String minutesFormat = "m";
        prependTimeAndUnit(timeBuf, minutes, minutesFormat);

        // hour(60m) & above
        time = time / 60;
        if (time < 1) {
            return timeBuf.toString();
        }

        long hours = time % 24;
        String hourFormat = "h";
        prependTimeAndUnit(timeBuf, hours, hourFormat);

        // day(24h) & above
        time = time / 24;
        if (time < 1) {
            return timeBuf.toString();
        }

        long day = time % 365;
        String dayFormat = " dní";
        if(day == 1){
            dayFormat = " den";
        } else if (day > 1 && day < 5){
            dayFormat = " dny";
        }
        prependTimeAndUnit(timeBuf, day, dayFormat);

        // year(365d) ...
        time = time / 365;
        if (time < 1) {
            return timeBuf.toString();
        }

        prependTimeAndUnit(timeBuf, time, "y");

        return timeBuf.toString();
    }

}
