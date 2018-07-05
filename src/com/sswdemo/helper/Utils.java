package com.sswdemo.helper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

/**********************************************
 * 常用工具类
 * @author nixuena
 *
 */
public class Utils {

	public static boolean sDebug = true;
	public static String sLogTag;
	private static final String ENCODING_UTF8 = "UTF-8";
	
	
	/***************************************
	 * 比较两个年月日期的大小
	 * @param year1 ： 年份1
	 * @param month1 ： 月份1
	 * @param year2 ： 年份2
	 * @param month2 : 月份2
	 * @return (1,0, -1)
	 * 注：1=日期1大于日期2
	 * 	 0=日期1等于日期2
	 * 	 -1=日期1小于日期2
	 */
	public static int compareDate(int year1, int month1, int year2, int month2){
		if (year1 > year2)
			return 1;
		else if (year1 == year2){
			if (month1 > month2)
				return 1;
			else if (month1 == month2)
				return 0;
			else
				return -1;
		}
		else
			return -1;
	}
	
	/***************************************
	 * 获取每月的天数
	 * @param month ： 月份
	 * @param year : 年份
	 * @return
	 */
	//二月还需要判断是否是润年，润年29天
	private static int[] dayOfMonth = {31,29,31,30,31,30,31,31,30,31,30,31};
	public static  int getDaysOfMonth(int year, int month){
		
		if (month == 1){
			boolean b = Utils.isAnnoBisestileYear(year);
			if (b)
				return 29;
			else
				return 28;
		}
		else 
			return dayOfMonth[month];			
	}
	
	/***************************************
	 * 计算2个日期的相差的天数:
	 * 计算该月份的第一天与开始服药日期之间相差的天数
	 * @param myDate
	 * @return int
	 */
//	public static int getDays(MyDate myDate1, MyDate myDate2){		
//		
//		Date da1 = new Date(myDate1.years, myDate1.months, myDate1.days);
//		Date da2 = new Date(myDate2.years, myDate2.months, myDate2.days);
//		
//		long tm1 = da1.getTime();
//		long tm2 = da2.getTime();
//		
//		long days = (tm1 - tm2) / (1000 * 60 * 60 * 24);
//		
//		return (int)days;
//	}
	/***********************************************
	 * 设置全屏显示（无标题栏，无状态栏）
	 * @param context
	 */
	public static void setFullScreen(Context context){
		setScreenNoTitle(context);
		setScreenNoStatus(context);					           
	}
	
	/**********************************************
	 * 设置无标题栏的样式
	 */
	public static void setScreenNoTitle(Context context){
		((Activity)context).requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	/*********************************************
	 * 设置屏幕没有状态栏
	 * @param context
	 */
	public static void setScreenNoStatus(Context context){
		((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	/*******************************************
	 * 判断是否为润年
	 * @param year
	 * @return
	 */
	public static boolean isAnnoBisestileYear(int year){
		if (year % 400 == 0)
			return true;
		else if (year % 4 == 0 && year % 100 != 0)
			return true;
		
		else 
			return false;
	}
	
	/****************************************************
     * 把一个字符串，转换成UTF-8编码的字节数组
     * 
     * @param string 需要转换的源字符串           
     * @return byte[] 转换后的字节数组
     */
    public static byte[] getUTF8Bytes(String string) {
        if (string == null)
            return new byte[0];

        try {
            return string.getBytes(ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            /*
             * If system doesn't support UTF-8, use another way
             */
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);
                dos.writeUTF(string);
                byte[] jdata = bos.toByteArray();
                bos.close();
                dos.close();
                byte[] buff = new byte[jdata.length - 2];
                System.arraycopy(jdata, 2, buff, 0, buff.length);
                return buff;
                
            } catch (IOException ex) {
                return new byte[0];
            }
        }
    }
    
   
    
    /********************************************
     * 把字符串中的通过正则匹配上的字符串替换成其他字符串
     * @param res : 源字符串
     * @param PatternStr ： 匹配字符串
     * @param replaceStr ： 替换字符串
     * @return String : 返回替换后的字符窜
     */
    public static String replace(String res, String PatternStr, String replaceStr){
      Pattern p = Pattern.compile(PatternStr);
  	  Matcher m = p.matcher(res);
  	  String after = m.replaceAll(replaceStr); 
  	  return after;
    }
    
	/****************************************************
	 * 把一个 Bitmap 对象转换成Drawable 对象
	 * @param bitmap ： 需要转换的 Bitmap 对象
	 * @return Drawable 
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap){
		if (bitmap == null)
			return null;
		
		return new BitmapDrawable(bitmap);		 
	}
	
	/****************************************************
	 * 比较2个字节数组中的内容是否相等
	 * @param a : 字节数组
	 * @param b : 字节数组
	 * @return boolean 
	 */
	public static boolean isEqual(byte[] a, byte[] b) {
        if (a == null || b == null) 
            return a == b;   
        
        else if (a.length != b.length)        
            return false;      
        else {
            for (int i = 0; i < b.length; i++) {
                if (a[i] != b[i]) {
                    return false;
                }
            }
            return true;
        }
    }
	
	/***************************************************
     * 检测SDCard是否可写
     */
    public static boolean isSdcardWritable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    
    /***************************************************
     * 检测SDCard是否可读
     */
    public static boolean isSdcardReadable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)
                || Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    
    /***************************************************
     * 获取手机屏幕的宽高
     * int[0]=宽
     * int[1]=高
     * @param context
     * @return int[]
     */
    public static int[] getScreenWidthAndHeight(Context context){
    	int[] screen = new int[2];
    	
    	if (context == null)
    		return screen;
    	
	    WindowManager manage = ((Activity)context).getWindowManager();	
	    Display display=manage.getDefaultDisplay();
		    
	    screen[0] = display.getWidth();
	    screen[1] = display.getHeight();
	
	    return screen;	      
    }
    
    /****************************************************
     * 解析二维码地址
     */
    public static HashMap<String, String> parserUri(Uri uri) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        String paras[] = uri.getQuery().split("&");
        for (String s : paras) {
            if (s.indexOf("=") != -1) {
                String[] item = s.split("=");
                parameters.put(item[0], item[1]);
            } else {
                return null;
            }
        }
        return parameters;
    }
    
    public static void V(String msg) {
        if (sDebug) {
            Log.v(sLogTag, msg);
        }
    }

    public static void V(String msg, Throwable e) {
        if (sDebug) {
            Log.v(sLogTag, msg, e);
        }
    }

    public static void D(String msg) {
        if (sDebug) {
            Log.d(sLogTag, msg);
        }
    }

    public static void D(String msg, Throwable e) {
        if (sDebug) {
            Log.d(sLogTag, msg, e);
        }
    }

    public static void I(String msg) {
        if (sDebug) {
            Log.i(sLogTag, msg);
        }
    }

    public static void I(String msg, Throwable e) {
        if (sDebug) {
            Log.i(sLogTag, msg, e);
        }
    }

    public static void W(String msg) {
        if (sDebug) {
            Log.w(sLogTag, msg);
        }
    }

    public static void W(String msg, Throwable e) {
        if (sDebug) {
            Log.w(sLogTag, msg, e);
        }
    }

    public static void E(String msg) {
        if (sDebug) {
            Log.e(sLogTag, msg);
        }
    }

    public static void E(String msg, Throwable e) {
        if (sDebug) {
            Log.e(sLogTag, msg, e);
        }
    }
    
    /******************************************
	 * 存储字符值
	 * 
	 * @author shilinl
	 * @date 2012-4-28 下午5:40:50
	 * @param activity
	 *            上下文
	 * @param name
	 *            存储名
	 * @param value
	 *            存储值
	 */
	public static void storeString(Activity activity, String name, String value)
	{
		// SharePreferences and Editor
		SharedPreferences preferences = activity.getSharedPreferences(
		        name, activity.MODE_PRIVATE);
		Editor editor = preferences.edit();

		editor.putString(name, value);
		editor.commit();
	}

	/*******************************************
	 * 获取字符值
	 * 
	 * @author shilinl
	 * @date 2012-4-28 下午5:41:35
	 * @param activity
	 *            上下文
	 * @param name
	 *            存储名
	 * @return String,默认为null
	 */
	public static String fetchString(Activity activity, String name)
	{
		// SharePreferences and Editor
		SharedPreferences preferences = activity.getSharedPreferences(name,
		        activity.MODE_PRIVATE);

		return preferences.getString(name, null);

	}
	/**
	 * 对字符分组
	 * @param str
	 * @param splitsign
	 * @return
	 */
	 public static String[] split2Array(String str,String splitsign){
		 int index;
		 if(null == str || null == splitsign)
			 return null;
		 
		 ArrayList<String> al = new ArrayList<String>();
		 
		 while((index = str.indexOf(splitsign))!= -1){
			 al.add(str.substring(0,index));
			 str = str.substring(index + splitsign.length());
		 }
		 al.add(str);
		 return (String[])al.toArray(new String[0]);
	 }
	 /**
	  * 对字符分组
	  * @param str
	  * @param splitsign
	  * @return
	  */
	 public static List<String> split2List(String str,String splitsign){
		 int index;
		 if(null == str || null == splitsign)
			 return null;
		 
		 ArrayList<String> al = new ArrayList<String>();
		 
		 while((index = str.indexOf(splitsign))!= -1){
			 al.add(str.substring(0,index));
			 str = str.substring(index + splitsign.length());
		 }
		 al.add(str);
		 return al;
	 }

	
}
