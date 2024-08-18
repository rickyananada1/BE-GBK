package com.dev.gbk.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utils
 */
public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

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
        logger.info("Generate booking number: {} {}", lastId, statusBooking);
        String code = "";
        if (statusBooking.equals("Maintenance")) {
            code = "ME";
        } else if (statusBooking.equals("Soft Booking")) {
            code = "SF";
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
        return code + year + month + day + (lastId + 1);
    }

    public static BigDecimal convertStringToBigDecimal(String number) {
        if (number == null || number.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Remove "Rp. " prefix if present
        number = number.replace("Rp ", "");

        // Define Indonesian locale for correct decimal and grouping separators
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));

        // Create DecimalFormat with Indonesian locale and grouping separator
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        decimalFormat.setParseBigDecimal(true);

        try {
            // Parse the string into a BigDecimal, handling grouping and decimal separators
            BigDecimal result = (BigDecimal) decimalFormat.parse(number);
            return result;
        } catch (ParseException e) {
            // Handle parsing errors, perhaps return BigDecimal.ZERO or throw an exception
            System.err.println("Error parsing number: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}