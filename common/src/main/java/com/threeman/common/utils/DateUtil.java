package com.threeman.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/2/14 16:44
 */
public class DateUtil {


    /**
     * 将java.util.Date 转换为java8 的java.time.LocalDateTime,默认时区为东8区
     * @param date 日期
     * @return LocalDateTime
     */
    public static LocalDateTime dateConvertToLocalDateTime(Date date) {
        return date.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }

    /**
     * 将java8 的 java.time.LocalDateTime 转换为 java.util.Date，默认时区为东8区
     * @param localDateTime 本地时间
     * @return Date
     */
    public static Date localDateTimeConvertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
    }

    /**
     * java设置相对时间
     */
    public static String relativeTime(Date date){
        long l = date.getTime() - localDateTimeConvertToDate(LocalDateTime.now()).getTime();
        String result;
        long b=l/1000*3600*24;
        if (b>=30&&b<365){
            b=b/30;
            result=b+"月前";
        }else if (b>=365){
            b=b/365;
            result=b+"年前";
        }else {
            result=b+"天前";
        }
        return result;
    }
}
