package com.sunjet.utils.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lhj on 16/9/16.
 */
public class DateHelper {
    public static Date getFirstOfMonth() {
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
//        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();

//        LocalDate localDate = LocalDate.now();
    }

    public static Date getFirstOfYear() {
        //获取当前年1月1日
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR, -12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getEndDateOfYear() {
        //获取当前年1月1日
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DATE, 31);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }


    public static Date getEndDateTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    public static Date getStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    public static Date getEndDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    public static String getTimeToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm");
        return sdf.format(date);
    }

    public static String getTime(Date date) {
        return "";
    }


    public static String dateToString(Date time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (time != null) {
            String ctime = formatter.format(time);
            return ctime;
        }
        return "";

    }

    //获取下个月的10号
    public static Date nextMonthTenthDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 10);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }


}
