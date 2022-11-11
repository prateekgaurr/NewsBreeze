package com.prateek.newsbreeze.room;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConvertor {

    @TypeConverter
    public static String dateToString(Date date){
        return (date == null) ? null : date.toString();
    }

    @TypeConverter
    public static Date stringToDate(String dateString){
        return (dateString == null || dateString.length()<1) ? null : new Date(dateString);
    }
}
