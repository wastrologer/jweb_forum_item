package com.common.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    /**
     * 获得指定日期增量的date
     *
     * @param add
     * @return
     * @throws Exception
     */
    public static Date getSpecifiedDay(Integer add) {//可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        c.add(Calendar.DATE, add);
        Date dayBefore = c.getTime();
        return dayBefore;
    }
}
