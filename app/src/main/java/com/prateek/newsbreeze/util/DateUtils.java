package com.prateek.newsbreeze.util;



import static java.time.temporal.ChronoUnit.DAYS;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static long calculateNoOfDays(Date fromDate, Date toDate){
        return DAYS.between(fromDate.toInstant(), toDate.toInstant());
    }

    public static String toDisplayDate(Date date){
        String pattern = "dd MMM yyyy";
        return new SimpleDateFormat(pattern).format(date);
    }
}
