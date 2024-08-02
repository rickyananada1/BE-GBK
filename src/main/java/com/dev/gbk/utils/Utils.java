package com.dev.gbk.utils;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Utils
 */
public class Utils {

    // convert string to local date
    public static LocalDate convertStringToLocalDate(String date) {
        return LocalDate.parse(date);
    }

    // convert string to local time
    // "ScheduleTimeFrom": "14:00",
    // "ScheduleTimeTo": "18:00",
    public static LocalTime convertStringToLocalTime(String time) {
        return LocalTime.parse(time);
    }

    public static String generateBookingNumber() {
        return String.valueOf(System.currentTimeMillis());
    }
}