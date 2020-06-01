package com.sunjet.frontend;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import sun.rmi.runtime.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: lhj
 * @create: 2017-11-24 16:43
 * @description: 说明
 */
public class CalendarTest {

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    @Test
    public void getPastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        Date today = calendar.getTime();
        System.out.println(today.toString());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        System.out.println(result);
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    @Test
    public void getFetureDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
        Date today = calendar.getTime();
        System.out.println(today.toString());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        System.out.println(result);
    }
}
