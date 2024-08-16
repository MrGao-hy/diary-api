package com.example.demo.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class TimeFormatConversionUtil {
    /**
     * 时间戳转化LocalDateTime类型 ==》 dateTime
     *
     * @param timestamp 时间戳
     * @return LocalDateTime
     */
    public LocalDateTime timestampToDateFormat(long timestamp) {
        // 将时间戳转换为Instant对象
        Instant instant = Instant.ofEpochMilli(timestamp);

        // 将Instant对象转换为LocalDateTime对象
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // 创建DateTimeFormatter对象，指定时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 使用DateTimeFormatter对象将LocalDateTime对象格式化为指定的时间格式
        String formattedDate = localDateTime.format(formatter);

        return localDateTime;
    }

    /**
     * 时间戳转化LocalDateTime类型 ==》 dateTime
     *
     * @param timestamp  时间戳
     * @param timeFormat "yyyy-MM-dd HH:mm:ss"
     * @return "2023-12-12"
     */
    public static String timestampToDateTime(long timestamp, String timeFormat) {
        // 创建SimpleDateFormat对象，指定时间格式
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);

        // 将时间戳转换为Date对象
        Date date = new Date(timestamp);

        // 使用SimpleDateFormat对象将Date对象格式化为指定的时间格式
        String formattedDate = sdf.format(date);


        return formattedDate;
    }
}
