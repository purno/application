package com.application.commons.utils;

import com.application.commons.constants.DatePatterns;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;


public class DateUtil {

    public static LocalDate convertStrToLocalDate(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }
    
	/**
	 * Converts yyyy-MM-dd to {@link DatePatterns#EMAIL_DATE_FORMAT}.
	 * NOTE: Do not make changes in this method as it's being used in velocity templates.
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String convertToEmailFormat(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePatterns.EMAIL_DATE_FORMAT);
		return LocalDate.parse(dateStr).format(formatter);
	}

    public static LocalDateTime convertStrToLocalDateTime(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateStr, formatter);
    }

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

    public static String convertTimeStampToString(Timestamp ts, String pattern) {
        return ts.toLocalDateTime().format(DateTimeFormatter.ofPattern(pattern));
    }

	public static LocalDateTime convertStrToLocalDateTimeDefault(String dateStr, String pattern) {
		
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern(pattern).parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter();
		return LocalDateTime.parse(dateStr, formatter);
	}


	public static LocalDateTime convertLocalDateToLocalDateTimeDefault(LocalDate localDate) {
		return convertLocalDateToLocalDateTime(localDate, LocalTime.of(0, 0, 0));
	}
	
	public static LocalDateTime convertLocalDateToLocalDateTime(LocalDate localDate, LocalTime localTime) {
		return LocalDateTime.of(localDate, localTime);
	}

}
