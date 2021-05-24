package com.junlin.utils;

import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DateUtils {
    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static List<Date> dateAllTimeWithThirtyMinute = new ArrayList<>();;

    static {

        SimpleDateFormat hmFormat = new SimpleDateFormat(DateUtils.DF_HH_mm);

        for (int h = 0, m = 0; h < 24; m += 30) {//创建循环，指定间隔五分钟
            if (m >= 60) {//判断分钟累计到60时清零，小时+1
                h++;
                m = 0;
            }
            if (h >= 24) {//判断小时累计到24时跳出循环，不添加到集合
                break;
            }

            /*转换为字符串*/
            String hour = String.valueOf(h);
            String minute = String.valueOf(m);

            /*判断如果为个位数则在前面拼接‘0’*/
            if (hour.length() < 2) {
                hour = "0" + hour;
            }
            if (minute.length() < 2) {
                minute = "0" + minute;
            }


            try {
                dateAllTimeWithThirtyMinute.add( hmFormat.parse(hour + ":" + minute));//拼接为HH:mm格式，添加到集合
            } catch (ParseException e) {
                logger.error("初始化日期数据失败!");
            }
        }
    }


    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public final static String DF_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public final static String DF_yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * yyyy-MM
     */
    public final static String DF_yyyy_MM = "yyyy-MM";
    /**
     * yyyy年MM月
     */
    public final static String DF_yyyy_MM2 = "yyyy年MM月";
    /**
     * yyyy-MM-dd
     */
    public final static String DF_yyyy_MM_dd = "yyyy-MM-dd";
    /**
     * yyyy年MM月dd日
     */
    public final static String DF_yyyy_MM_dd2 = "yyyy年MM月dd日";
    /**
     * HH:mm:ss
     */
    public final static String DF_HH_mm_ss = "HH:mm:ss";
    /**
     * MM-dd
     */
    public final static String DF_MM_dd = "MM-dd";
    
    /**
     * HH:mm:ss.SSS
     */
    public final static String DF_HH_mm_ss_SSS = "HH:mm:ss.SSS";
    /**
     * yyyy
     */
    public final static String DF_yyyy = "yyyy";
    /**
     * yyyy
     */
    public final static String DF_yy = "yy";
    /**
     * yyyyMM
     */
    public final static String DF_yyyyMM = "yyyyMM";
    /**
     * yyyyMMdd
     */
    public final static String DF_yyyyMMDD = "yyyyMMdd";
    /**
     * yyyyMMddHHmmssSSS
     */
    public final static String DF_yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
    /**
     * yyyyMMddHHmmss
     */
    public final static String DF_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    /**
     * MM-dd mm:ss
     */
    public final static String DF_MM_dd_HH_mm = "MM-dd HH:mm";
    /**
     * HH:mm
     */
    public final static String DF_HH_mm = "HH:mm";
    /**
     * yyyy-MM-dd HH:mm
     */
    public final static String DF_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    /**
     * yyyy年MM月dd日 HH:mm
     */
    public final static String DF_yyyy_MM_dd_HH_mm2 = "yyyy年MM月dd日 HH:mm";
    /**
     * yyyy年MM月dd日 HH:mm:ss
     */
    public final static String DF_yyyy_MM_dd_HH_mm3 = "yyyy年MM月dd日 HH:mm:ss";
    /**
     * yyyy年MM月dd日 HH:mm:ss
     */
    public final static String DF_MM_dd2 = "MM月dd日";
    /**
     * MM/dd
     */
    public final static String DF_MM_dd3 = "MM/dd";
    
    /**
     * 时区GMT+8
     */
    public final static String TZ_GMT_8 = "GMT+8";

    /**
     * 得到unix时间戳
     *
     * @return
     */
    public static int getUnixTimeStamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 获取两个月份之间的月份
     * @param startMonth yyyy-MM
     * @param endMonth yyyy-MM
     * @return
     */
    public static List<String> getBetweenMonth(String startMonth, String endMonth) {
        List<String> monthList = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        startCal.setTime(str2Date(startMonth, DF_yyyy_MM));
        startCal.set(startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH), 1);

        endCal.setTime(str2Date(endMonth, DF_yyyy_MM));
        endCal.set(endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH), 2);
        while (startCal.before(endCal)) {
            monthList.add(date2Str(startCal.getTime(), DF_yyyyMM));
            startCal.add(Calendar.MONTH, 1);
        }
        return monthList;
    }

    /**
     * 获取时间段内日期
     * @param start
     * @param end
     * @return
     */
    public static List<Date> getBetweenDate(Date start, Date end) {
        List<Date> dateList = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        startCal.set(Calendar.HOUR_OF_DAY, 23);
        startCal.set(Calendar.MINUTE, 59);
        startCal.set(Calendar.SECOND, 59);
        startCal.set(Calendar.MILLISECOND, 999);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);
        end = endCal.getTime();
        dateList.add(startCal.getTime());
        while (end.after(startCal.getTime())) {
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(startCal.getTime());
        }
        return dateList;
    }

    /**
     * 获取时间段内日期
     * @param startStr yyyy-MM-dd
     * @param endStr yyyy-MM-dd
     * @return
     */
    public static List<Date> getBetweenDate(String startStr, String endStr){
        Date start = str2Date(startStr, DF_yyyy_MM_dd);
        Date end = str2Date(endStr, DF_yyyy_MM_dd);
        return getBetweenDate(start, end);
    }

    public static String getWeekName(int dayOfWeek) {
        if (Calendar.MONDAY == dayOfWeek) {
            return "星期一";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            return "星期二";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            return "星期三";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            return "星期四";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            return "星期五";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            return "星期六";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            return "星期日";
        } else {
            return "日期错误";
        }
    }

    public static String getWeekDay(Date date){
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week < 0)
            week = 0;
        return weekDays[week];
    }

    public static String getWeekDesc(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return DateUtils.getWeekName(calendar.get(Calendar.DAY_OF_WEEK));
    }

    // 由日期字符串和日期格式获取日期对象
    public static Date str2Date(String dateStr, String datePatten) {
        SimpleDateFormat format = new SimpleDateFormat(datePatten);
        try {
            Date date = format.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转换字符串
     * @param date
     * @return
     */
    public static String date2Str(Date date){
        return date2Str(date, DF_yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 日期转换字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String date2Str(Date date,String pattern){
        if (date == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     *时间戳转化为周几；
     */
    public static String timestamp2Dayweek(Integer timestamp){
        GregorianCalendar cal = new GregorianCalendar();
        long time = timestamp;
        cal.setTime(new Date(time*1000));
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        System.out.println(timestamp*1000+"---------dayWeek-----"+dow);
        switch (dow) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
        }
        return "Unknown";
    }

    /**
     * 整数(秒数)转换为时分秒格式(xx:xx:xx)
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    // 由时间戳得到日期字符串
    public static String unixTimeStampInteger2DateString(Integer timeStamp, String format) {
        long l=(long)timeStamp.intValue()*1000;
        SimpleDateFormat datetimefmt = new SimpleDateFormat(format);
        Date date= new Date(l);
        String retStr = datetimefmt.format(date);
        return retStr;
    }

    // 由(Long)时间戳得到日期字符串
    public static String unixTimeStampLong2DateString(Long timeStamp, String format) {
        SimpleDateFormat datetimefmt = new SimpleDateFormat(format);
        Date date= new Date(timeStamp);
        String retStr = datetimefmt.format(date);
        return retStr;
    }

    // 由时间戳得到hh:mma格式
    public static String timeStampToHourString(Integer timeStamp) {
        long l=(long)timeStamp.intValue()*1000;
        SimpleDateFormat datetimefmt = new SimpleDateFormat("HH:mma", Locale.ENGLISH);
        Date date= new Date(l);
        String retStr = datetimefmt.format(date).toLowerCase();
        return retStr;
    }

    // 由时间戳得到日期字符串
    public static Date unixTimeStampInteger2Date(Integer timeStamp) {
        long l=(long)timeStamp.intValue()*1000;
        Date date= new Date(l);
        return date;
    }

    public static Date unixDataStringToDate(String dataString,String format) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date parsedDate = dateFormat.parse(dataString);
        return parsedDate;

    }

    /**
     * 两个date相差的秒数
     * @param d1
     * @param d2
     * @return
     */
    public static Integer differSecond(Date d1, Date d2){
        if (d1 == null || d2 == null) {
            return 0;
        }
        int second = (int)((d1.getTime() - d2.getTime())/1000);
        return second < 0 ? - second : second;
    }

    /**
     * 两个日期相差天数
     * @param startTime
     * @param endTime
     * @param datePattern
     * @return
     */
    public static Integer differDay(String startTime, String endTime, String datePattern){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(datePattern);
        LocalDate start = LocalDate.parse(startTime, df);
        LocalDate end = LocalDate.parse(endTime, df);
        return (int)(end.toEpochDay() - start.toEpochDay());
    }

    /**
     * 获取月份的开始时间
     * @param monthStr
     * @return
     */
    public static Date getStartTimeOfMonth(String monthStr){
        return str2Date(monthStr + "-01 00:00:00", DateUtils.DF_yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 获取月份的结束时间
     * @param monthStr
     * @return
     */
    public static Date getEndTimeOfMonth(String monthStr){
        String [] strArr = monthStr.split("-");
        int year = Integer.parseInt(strArr[0]);
        int month = Integer.parseInt(strArr[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
	 * 传入日期获取 日期该月 的第一天
	 * @param paramDate
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date paramDate){
		Calendar cal = Calendar.getInstance();
		cal.setTime(paramDate);
		//获取某月最小天数
	    int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
	    //设置日历中月份的最小天数
	    cal.set(Calendar.DAY_OF_MONTH, firstDay);
	    return cal.getTime();   
	}
	
	/**
	 * 传入日期 获取该 日期该月 的最后一天
	 * @param paramDate
	 * @return
	 */
	public static Date getLastDayOfMonth(Date paramDate){
		Calendar cal = Calendar.getInstance();
		cal.setTime(paramDate);
	    //获取某月最大天数
	    int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    //设置日历中月份的最大天数
	    cal.set(Calendar.DAY_OF_MONTH, lastDay);
	    return cal.getTime();   
	}
	
	/**
     * 传入日期 格式化为 当日 00:00:00
     * @param date
     * @return
     */
    public static Date getStartTimeOfDate(Date date) {
        if (ObjectUtil.isNull(date))
            date = new Date();
    	Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY,00);
		c.set(Calendar.MINUTE,00);
		c.set(Calendar.SECOND,00);
		return c.getTime();
    }

    /**
     * 传入日期 格式化为 当日 23:59:59
     * @param date
     * @return
     */
    public static Date getEndTimeOfDate(Date date) {
        if (ObjectUtil.isNull(date))
            date = new Date();
    	Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY,23);
		c.set(Calendar.MINUTE,59);
		c.set(Calendar.SECOND,59);
		return c.getTime();
    }
    
    /**
     * 传入日期获取该日期在当月中是第几日
     * @param paramDate
     * @return
     */
    public static int getDayOfMonth(Date paramDate) {
    	Calendar c = Calendar.getInstance();
        c.setTime(paramDate);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取两点之间每隔半30分钟的数据
     * @param timeStart
     * @param timeEnd
     * @param dateFormat 为了节省内存 (请传)
     * @return  返回点数据 包含了开始时间点和结束时间点
     *   未做严格校验,调用者自己测
     */
    public static List<String> getTimePointListWithThirtyMinute(Date timeStart,Date timeEnd,SimpleDateFormat dateFormat){

        return DateUtils.dateAllTimeWithThirtyMinute.stream()
                .filter( date -> timeStart.before(date) || dateFormat.format(date).equals(dateFormat.format(timeStart)) )
                .filter( date -> timeEnd.after( date) || dateFormat.format(date).equals(dateFormat.format(timeEnd)) )
                .map( dateFormat::format )
                .collect( Collectors.toList() );
    }


    public static void main(String[] args) throws ParseException {
//        List<String> timeLagThirtyMinute = DateUtils.dateAllTimeWithThirtyMinute;
//        System.out.println("ddd");
//        List<Date> betweenDate = DateUtils.getBetweenDate("2021-05-11", "2021-05-15");

        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.DF_yyyy_MM_dd);
        SimpleDateFormat hmFormat = new SimpleDateFormat(DateUtils.DF_HH_mm);
        List<String> timePointListWithThirtyMinute = DateUtils.getTimePointListWithThirtyMinute(hmFormat.parse("10:00"), hmFormat.parse("12:00"), hmFormat);
        System.out.println("ddd");

    }

}
