package com.sswdemo.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class CallPhoneUtil {

	/**
     * 拨通电话
     */
    public static void callPhone(Activity activity, String phoneNumber) {
    	if (phoneNumber == null || phoneNumber.length() < 1) {
			return;
		}
    	Uri uri = Uri.parse("tel:"+phoneNumber);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        activity.startActivity(intent);
    }
}
