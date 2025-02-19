package com.seeg2.logicable.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Time {
    public static String zonedDateTimeHHMMSS() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }
}
