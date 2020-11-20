package com.code4j.util;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author liu_wp
 * @date 2020/11/6
 * @see
 */
public class DateUtil {
    public final static String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    public final static String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * @param format
     * @return
     */
    public static String getNowDate(String format) {
        format = format == null ? DEFAULT_DATE_FORMAT : format;
        return LocalDate.now().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * @param date
     * @param format
     * @return
     */
    public static String date2String(Date date, String format) {
        if (date != null) {
            format = format == null ? DEFAULT_DATETIME_FORMAT : format;
            Instant instant = date.toInstant();
            return instant.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(format));
        }
        return null;
    }

    /**
     * @param date
     * @param formatter
     * @return
     */
    public final static String dateFormatter(Date date, String formatter) {
        try {
            if (date == null) {
                return null;
            }
            if (StringUtils.isBlank(formatter)) {
                formatter = DEFAULT_DATETIME_FORMAT;
            }
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern(formatter));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param date
     * @param originalFormat
     * @return
     */
    public final static LocalDateTime date2LocalDateTime(String date, String originalFormat) {
        if (StringUtils.isBlank(originalFormat)) {
            originalFormat = DEFAULT_DATETIME_FORMAT;
        }
        return LocalDateTime.parse(date, DateTimeFormatter
                .ofPattern(originalFormat));
    }

    /**
     * @param dateTime
     * @param formatter
     * @return
     */
    public final static String dateTimeFormatter(Long dateTime, String formatter) {
        Date date = new Date(dateTime);
        return dateFormatter(date, formatter);
    }
    
}
