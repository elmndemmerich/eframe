package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 时间工具类
 * <br>
 * @date 2013-1-6
 * @author LiangRL
 * @alias E.E.
 */
public class DateUtil {
  
    public static final String YMD = "yyyy-MM-dd";
 
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    
    public static final String NUM_YMD = "yyyyMMdd";
    
    public static final String NUM_YMDHMS = "yyyyMMddHHmmss";	
    
    /**
     * 取出当前时间
     * @param format
     * @return
     */
    public static String getCurrentDateStr(String format){
    	SimpleDateFormat sdm = new SimpleDateFormat(format);
    	return sdm.format(new Date());
    }
    
    /**
     * date对象转化为时间格式。
     * @param date
     * @param format
     * @return
     */
    public static String date2str(Date date, String format){
    	SimpleDateFormat sdm = new SimpleDateFormat(format);
    	return sdm.format(date);
    }
    
    /**
     * 字符串形式的时间转化为date对象
     * @param timeStr
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date str2Date(String timeStr, String format) throws ParseException{
    	SimpleDateFormat sdm = new SimpleDateFormat(format);
    	return sdm.parse(timeStr);
    }
    
    /**
     * 从JWS中找到的好东西
     * <br>
     * @date 2013-1-10
     * @author LiangRL
     * @alias E.E.
     */
    public static class AlternativeDateFormat {
    	
    	static ThreadLocal<AlternativeDateFormat> dateformat = new ThreadLocal<AlternativeDateFormat>();
    	
        Locale locale;
        List<SimpleDateFormat> formats = new ArrayList<SimpleDateFormat>();

        public AlternativeDateFormat(Locale locale, String... alternativeFormats) {
            super();
            this.locale = locale;
            setFormats(alternativeFormats);
        }

        public void setFormats(String... alternativeFormats) {
            for (String format : alternativeFormats) {
                formats.add(new SimpleDateFormat(format, locale));
            }
        }

        public Date parse(String timeStr) throws ParseException {
            for (SimpleDateFormat dateFormat : formats) {
                if (timeStr.length() == dateFormat.toPattern().replace("\'", "").length()) {
                	return dateFormat.parse(timeStr);
                }
            }
            throw new ParseException("Date format not understood", 0);
        }

        public static AlternativeDateFormat getDefaultFormatter() {
            if (dateformat.get() == null) {
                dateformat.set(new AlternativeDateFormat(Locale.US,
                        "yyyy-MM-dd'T'HH:mm:ss'Z'", // ISO8601 + timezone
                        "yyyy-MM-dd'T'HH:mm:ss", // ISO8601
                        "yyyy-MM-dd HH:mm:ss",
                        "yyyyMMdd HHmmss",
                        "yyyy-MM-dd",
                        "yyyyMMdd'T'HHmmss",
                        "yyyyMMddHHmmss",
                        "dd'/'MM'/'yyyy",
                        "dd-MM-yyyy",
                        "dd'/'MM'/'yyyy HH:mm:ss",
                        "dd-MM-yyyy HH:mm:ss",
                        "ddMMyyyy HHmmss",
                "ddMMyyyy"));
            }
            return dateformat.get();
        }
    }    
}
