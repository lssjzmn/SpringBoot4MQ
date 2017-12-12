package application.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lancec on 2014/5/24.
 */
public class DateUtil {
    public static String getDateRangeString(Date startDate, Date endDate, String msg) {
        if (startDate == null || endDate == null) {
            return "";
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        if (endCalendar.before(startCalendar)) {
            return msg;
        }

        long millis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
        int days = (int) (millis / (1000 * 60 * 60 * 24));
        long hoursMillis = millis % (1000 * 60 * 60 * 24);
        int hours = (int) (hoursMillis / (1000 * 60 * 60));
        long minutesMillis = hoursMillis % (1000 * 60 * 60);
        int minutes = (int) (minutesMillis / (1000 * 60));

        if (days == 0) {
            return hours + "小时" + minutes + "分";
        }
        return days + "天" + hours + "小时" + minutes + "分";
    }

    public static int getDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return -1;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        int startYear = startCalendar.get(Calendar.YEAR);
        int endYear = endCalendar.get(Calendar.YEAR);

        int startDay = startCalendar.get(Calendar.DAY_OF_YEAR);
        int endDay = endCalendar.get(Calendar.DAY_OF_YEAR);
        if (startYear != endYear) {
            endDay = startCalendar.getActualMaximum(Calendar.DAY_OF_YEAR) + endDay;
        }

        int days = endDay - startDay;
        return days;
    }

    public static  String getTimeStr(Date time){
        if(time==null){
            return "";
        }
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);
        return ctime;
    }

    public static Date getDateByDay(int day) {
        return getDateByDay(new Date(), day);
    }

    public static Date getDateByDay(Date fromDate, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        calendar.add(Calendar.DAY_OF_YEAR, day);
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static String getDateBeforeString(Date beforeDate) {
        long currentMills = System.currentTimeMillis();
        long beforeMills = beforeDate.getTime();

        long time = currentMills - beforeMills;
        if (time < 1000) {
            return "刚刚";
        }
        time = time / 1000;
        if (time < 60) {
            return time + "秒前";
        }
        time = time / 60;
        if (time < 60) {
            return time + "分钟前";
        }
        time = time / 60;
        if (time < 24) {
            return time + "小时前";
        }
        time = time / 24;
        if (time < 31) {
            return time + "天前";
        }
        time = time / 30;
        if (time < 12) {
            return time + "个月前";
        }
        time = time / 12;
        return time + "年前";
    }

    public static boolean isInSameWeek(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        int c1f = c1.get(Calendar.WEEK_OF_YEAR);
        int c2f = c2.get(Calendar.WEEK_OF_YEAR);
        return c1f == c2f;
    }



}
