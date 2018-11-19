package com.demo.zhang.util;

import java.util.Calendar;

public class DateOperatorUtil {
    public static String getTwoDigits(int num){
        if(num > 9){
            return new Integer(num).toString();
        }else{
            return "0" + num;
        }
    }

    public static String getShowTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
//        return year + "-" + month + "-" + date + " " + hour + ":" + minute;
        return getTwoDigits(hour) + ":" + getTwoDigits(minute);
    }
}
