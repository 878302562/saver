package com.example.kangpei.saver.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by kangpei on 20/11/16.
 */

public class TimeUtils {

    //获取当前月的一天
    public static long getFirstDayofMonth(){
        Calendar currentDate=new GregorianCalendar();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY,0);
        currentDate.set(Calendar.MINUTE,0);
        currentDate.set(Calendar.SECOND,0);
        currentDate.set(Calendar.DAY_OF_MONTH,0);
        return currentDate.getTime().getTime();
    }
}
