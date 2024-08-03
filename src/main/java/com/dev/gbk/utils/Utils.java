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

    public static String generateBookingNumber(Long lastId, String statusBooking) {
        String code = "";
        if (statusBooking == "Maintenance") {
            code = "MB";
        } else {
            code = "BO";
        }
        // tanggal bulan dan 2 digit di akhir tahun
        String year = String.valueOf(LocalDate.now().getYear()).substring(2, 4);
        String month = String.valueOf(LocalDate.now().getMonthValue());
        String day = String.valueOf(LocalDate.now().getDayOfMonth());
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        return code + "-" + year + month + day + "-" + (lastId + 1) + "-" + statusBooking;
    }
}