package com.prateek.newsbreeze.room;

import androidx.room.TypeConverter;

import com.prateek.newsbreeze.models.Source;

public class SourceConvertor {

    @TypeConverter
    public static String sourceToString(Source source){
        return source == null ? null : source.name;
    }

    @TypeConverter
    public static Source stringToSource(String sourceName){
        return (sourceName == null || sourceName.length()<1) ? null : new Source(sourceName);
    }
}
