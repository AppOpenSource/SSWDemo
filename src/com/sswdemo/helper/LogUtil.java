package com.sswdemo.helper;

import android.util.Log;

public class LogUtil {

	private static int DEBUG = 0;

	public static void d(String tag, String msg) {
		switch (DEBUG) {
		case 1:
			Log.d(tag, msg);
			break;
		}
	}

	public static void e(String tag, String msg) {
		switch (DEBUG) {
		case 1:
			Log.e(tag, msg);
			break;
		}
	}

	public static void i(String tag, String msg) {
		switch (DEBUG) {
		case 1:
			Log.i(tag, msg);
			break;
		}
	}

	public static void v(String tag, String msg) {
		switch (DEBUG) {
		case 1:
			Log.v(tag, msg);
			break;
		}
	}
	
	public static void w(String tag, String msg) {
		switch (DEBUG) {
		case 1:
			Log.w(tag, msg);
			break;
		}
	}
}
