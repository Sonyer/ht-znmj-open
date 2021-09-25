package com.handturn.bole.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具类
 *
 * @author Eric
 */
public class DateUtil {

    public static final String DATA_PATTERN = "yyyy-MM-dd";

    public static final String DATA_PATTERN_8 = "yyyyMMdd";

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String CST_TIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";

    public static final String CN_DATE_SPLIT_PATTERN = "yyyy年MM月dd日";

    public static final String CN_TIMS_SPLIT_PATTERN = "yyyy年MM月dd日 HH:mm";

    //日期验证正则表达式
    public static final String yearPattern = "^2[0-9]{3}$";
    public static final String monthPattern = "^2[0-9]{3}-(0?[1-9]|1[0-2])$";
    public static final String janPattern = "(0?[13578]|1[02])-(0?[1-9]|[12][0-9]|3[01])";
    public static final String febPattern = "0?2-(0?[1-9]|[12][0-9])";
    public static final String aprPattern = "(0?[469]|11)-(0?[1-9]|[12][0-9]|30)";
    public static final String dayPattern = String.format("^2[0-9]{3}-(%s|%s|%s)$", janPattern, febPattern, aprPattern);
    public static final String hourFormat = String.format("^2[0-9]{3}-(%s|%s|%s) ([01][0-9]|2[0-3]):00:00$", febPattern, janPattern, aprPattern);
    public static final String timeFormat = String.format("^2[0-9]{3}-(%s|%s|%s) ([01][0-9]|2[0-3])(:[0-5][0-9]){2}$", febPattern, janPattern, aprPattern);


    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    public static String formatFullTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String getDateFormat(Date date, String dateFormatType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatType, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String formatCSTTime(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CST_TIME_PATTERN, Locale.US);
        Date usDate = simpleDateFormat.parse(date);
        return DateUtil.getDateFormat(usDate, format);
    }

    public static Date formatDate2Date(Date date, String dateFormatType) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatType, Locale.CHINA);
        return formatStr2Date(simpleDateFormat.format(date),dateFormatType);
    }

    public static Date formatStr2Date(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Date usDate = simpleDateFormat.parse(date);
        return usDate;
    }


    public static String formatInstant(Instant instant, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

  /*  public static boolean isDate(String str,String formatStr) {
        boolean result = true;

        SimpleDateFormat format = new SimpleDateFormat(formatStr);

        try {
            Date date = format.parse(str);
        }catch (Exception e){
            result = false;
        }
        return result;
    }*/

    public static boolean isDate(String str,String reg) {
        boolean result = true;

        // 指定好正则表达式
        Pattern  p = Pattern.compile(reg) ;
        // 实例化Pattern类
        Matcher m = p.matcher(str) ;
        // 实例化Matcher类
        if(m.matches()){
            // 进行验证的匹配，使用正则
            result = true;
        }else{
            result = false;
        }

        return result;
    }

    public static Date days(Date date,Integer days){
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        Date date2=calendar.getTime();
        return date2;
    }

    //校验8位字符串是否为正确的日期格式
    public static boolean isValidDate(String str,String formatStr) {
        boolean result = true;
        //判断字符串长度是否为8位
        if(str.length() == formatStr.length()){
            // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
            //SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            try {
                // 设置lenient为false.
                // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
                format.setLenient(false);
                format.parse(str);
            } catch (ParseException e) {
                // e.printStackTrace();
                // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
                result = false;
            }
        }else{
            result = false;
        }

        return result;
    }

    public  static void main(String[] args){
        System.out.println(isDate("20000211",dayPattern));
    }
}
