package com.dev.gbk.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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

    // convert string to Integer by dot, example: 100.000.000 => 100000000
    public static Integer convertStringToInteger(String number) {
        if (number == null || number.isEmpty()) {
            return 0;
        }
        // Remove all non-digit characters except for a single decimal point
        String cleanedNumber = number.replaceAll("[^\\d.]", "");

        // If there are multiple decimal points, keep only the last one
        if (cleanedNumber.chars().filter(ch -> ch == '.').count() > 1) {
            int lastDotIndex = cleanedNumber.lastIndexOf('.');
            cleanedNumber = cleanedNumber.substring(0, lastDotIndex)
                    + cleanedNumber.substring(lastDotIndex).replace(".", "");
        }

        // If there's a decimal point, remove it and parse as an integer
        // (assuming it represents thousands separators)
        if (cleanedNumber.contains(".")) {
            cleanedNumber = cleanedNumber.replace(".", "");
        }

        return Integer.parseInt(cleanedNumber);
    }
}