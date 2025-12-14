package com.sdut.hospital.Util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * @auther:chaoe
 * @date:2024/7/8
 **/


public class DateUtil {
    public static Date getTodayDate(){
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        return date;
    }
}
