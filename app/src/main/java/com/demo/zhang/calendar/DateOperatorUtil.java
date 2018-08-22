package com.demo.zhang.calendar;

public class DateOperatorUtil {
    public static String getTwoDigits(int num){
        if(num > 9){
            return new Integer(num).toString();
        }else{
            return "0" + num;
        }
    }
}
