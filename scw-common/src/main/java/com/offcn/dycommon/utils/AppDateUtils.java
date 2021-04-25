package com.offcn.dycommon.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppDateUtils {
    public static String getFomat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        String date = sdf.format(new Date());
        return date;
    }

    public static String getFormat(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(new Date());
        return date;
    }

    public static String getFormat(String format,Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}
