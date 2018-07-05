/*
 * comlan
 */

package com.abt.ssw.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
 
public class DateFormatUtil {
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_PATTERN_1 = "MM/dd HH:mm";
	public static String getCurrDate() {
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		String dStr = sdf.format(date);
		return dStr;
	}
	public static String getCurrDateByPattern(String pattern) {
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dStr = sdf.format(date);
		return dStr;
	}
	public static String getCurrDate(long time) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		String dStr = sdf.format(new Date(time));
		return dStr;
	}
	
	public static String getComparedFormateDateString(long time) {
		String DATE_FORMAT_PATTERN1 = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN1);
		String dStr = sdf.format(new Date(time));
		return dStr;
	}
	/**
	 * format time.[HH:]mm:ss
	 * @author mfy
	 * @param t
	 * @return
	 */
	public static String formatTime(long t){

		int hour = 0;
		int min = 0;
		int sec = 0;
		String formatedTime = "";
		
		if(t > 60 * 60 * 1000){
			hour = (int) (t / (60 * 60 * 1000));
			t -= hour * 60 * 60 *1000;
		}
		if(t > 60 * 1000){
			min = (int) (t / (60 * 1000));
			t -= min * 60 * 1000;
		}
		if(t > 1000){
			sec = (int) (t / 1000);
			t -= sec * 1000;
		}
		
		if(hour >= 10){
			formatedTime += hour + ":";
		}else if(hour > 0 && hour < 10){
			formatedTime += "0" + hour + ":";
		}
		
		if(min >= 10 && min < 60){
			formatedTime += min + ":";
		}else if(min > 0 && min < 10 ){
			formatedTime += "0" + min + ":";
		}else{
			formatedTime += "00" + ":";
		}
		
		
		if(sec >= 10 && sec < 60){
			formatedTime += sec + "";
		}else if(sec > 0 && sec < 10){
			formatedTime += "0" + sec ;
		}
		else{
			formatedTime += "00";
		}
		return formatedTime;
	}
	
}
