package com.generatefiles.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");

    public static String convertDateToString(LocalDateTime date, DateTimeFormatter formatter){
        return date.format(formatter);
    }

}
