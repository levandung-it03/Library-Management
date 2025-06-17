package com.homework.library_management.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeHelper {

    public static String localDateTimeToStr(LocalDateTime localDateTime) {
        var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    public static String localDateMilliTimeToStr(LocalDateTime localDateTime) {
        var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    public static LocalDateTime strToLocalDateTime(String str) {
        try {
            var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.from(sdf.parse(str).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
