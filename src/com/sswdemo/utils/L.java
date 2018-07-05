/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package com.sswdemo.utils;

import android.util.Log;

/**
 * Log to Android logging system.
 * 
 * Log message can be a string or a printf formatted string with arguments.
 * See http://developer.android.com/reference/java/util/Formatter.html
 */
public class L {
    
    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;
	private static final String TAG = "L";

    // Current log level
    public static int logLevel = Log.VERBOSE;
    
    /**
     * Set the current log level.
     * 
     * @param logLevel
     */
    public static void setLogLevel(int logLevel) {
        L.logLevel = logLevel;
        Log.i(TAG, "Changing log level to " + logLevel);
    }
    
    /**
     * Set the current log level.
     * 
     * @param logLevel
     */
    public static void setLogLevel(String logLevel) {
        if ("VERBOSE".equals(logLevel)) L.logLevel = VERBOSE;
        else if ("DEBUG".equals(logLevel)) L.logLevel = DEBUG;
        else if ("INFO".equals(logLevel)) L.logLevel = INFO;
        else if ("WARN".equals(logLevel)) L.logLevel = WARN;
        else if ("ERROR".equals(logLevel)) L.logLevel = ERROR;
        Log.i(TAG, "Changing log level to " + L.logLevel + "(" + logLevel + ")");
    }

    /**
     * Determine if log level will be logged
     * 
     * @param logLevel
     * @return
     */
    public static boolean isLoggable(int logLevel) {
        return (L.logLevel >= logLevel);
    }

    /**
     * Verbose log message.
     * 
     * @param tag
     * @param s
     */
    public static void v(String tag, String s) {
        if (L.VERBOSE < logLevel) return;
        
        Log.v(tag, s);
    }

    /**
     * Debug log message.
     * 
     * @param tag
     * @param s
     */
    public static void d(String tag, String s) {
        if (L.DEBUG < logLevel) return;

        Log.d(tag, s);   
    }
    
    /**
     * Info log message.
     * 
     * @param tag
     * @param s
     */
    public static void i(String tag, String s) {
        if (L.INFO < logLevel) return;
        Log.i(tag, s);
    }

    /**
     * Warning log message.
     * 
     * @param tag
     * @param s
     */
    public static void w(String tag, String s) {
        if (L.WARN < logLevel) return;
        Log.w(tag, s);
    }

    /**
     * Error log message.
     * 
     * @param tag
     * @param s
     */
    public static void e(String tag, String s) {
        if (L.ERROR < logLevel) return;
        Log.e(tag, s);
    }

    /**
     * Verbose log message.
     * 
     * @param tag
     * @param s
     * @param e
     */
    public static void v(String tag, String s, Throwable e) {
        if (L.VERBOSE < logLevel) return;
        Log.v(tag, s, e);
    }

    /**
     * Debug log message.
     * 
     * @param tag
     * @param s
     * @param e
     */
    public static void d(String tag, String s, Throwable e) {
        if (L.DEBUG < logLevel) return;
        Log.d(tag, s, e);
    }
    
    /**
     * Info log message.
     * 
     * @param tag
     * @param s
     * @param e
     */
    public static void i(String tag, String s, Throwable e) {
        if (L.INFO < logLevel) return;
        Log.i(tag, s, e);
    }

    /**
     * Warning log message.
     * 
     * @param tag
     * @param s
     * @param e
     */
    public static void w(String tag, String s, Throwable e) {
        if (L.WARN < logLevel) return;
        Log.w(tag, s, e);
    }

    /**
     * Error log message.
     * 
     * @param tag
     * @param s
     * @param e
     */
    public static void e(String tag, String s, Throwable e) {
        if (L.ERROR < logLevel) return;
        Log.e(tag, s, e);
    }

    /**
     * Verbose log message with printf formatting.
     * 
     * @param tag
     * @param s
     * @param args
     */
    public static void v(String tag, String s, Object... args) {
        if (L.VERBOSE < logLevel) return;
        Log.v(tag, String.format(s, args));
    }

    /**
     * Debug log message with printf formatting.
     * 
     * @param tag
     * @param s
     * @param args
     */
    public static void d(String tag, String s, Object... args) {
        if (L.DEBUG < logLevel) return;
        Log.d(tag, String.format(s, args));
    }

    /**
     * Info log message with printf formatting.
     * 
     * @param tag
     * @param s
     * @param args
     */
    public static void i(String tag, String s, Object... args) {
        if (L.INFO < logLevel) return;
        Log.i(tag, String.format(s, args));
    }
    
    /**
     * Warning log message with printf formatting.
     * 
     * @param tag
     * @param s
     * @param args
     */
    public static void w(String tag, String s, Object... args) {
        if (L.WARN < logLevel) return;
        Log.w(tag, String.format(s, args));
    }
    
    /**
     * Error log message with printf formatting.
     * 
     * @param tag
     * @param s
     * @param args
     */
    public static void e(String tag, String s, Object... args) {
        if (L.ERROR < logLevel) return;
        Log.e(tag, String.format(s, args));
    }

    
    private static boolean isFilteredLog(int level,String s){
    	if(s.contains("") && level == L.DEBUG) return true;
    	return false;
    }
    
}
