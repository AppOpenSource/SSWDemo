package com.sswdemo.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
public class NotificationUtil {
    public static final int AUTH_ERROR_ID = 4;
    public static final int QUOTA_ID = 3;
    public static final int STORAGE_ID = 2;
    public static final int UPLOAD_FAILED_ID = 11;
    public static final int UPLOAD_SUCCESS_ID = 10;
    public static final int UPDATE = 12;//更新
    public static final int MESSAGE = 13;//消息流通知
    public static final int CHARTMESSAGE = 14;//消息流通知
    
    
    /**
     * 状态栏通知
     * @param context 上下文对象 
     * @param id	通知id
     * @param contentTitle 标题
     * @param contentText 内容
     * @param intent 触发事件
     * @param icon 通知图标
     */
    public static void notifyStatus(Context context, int id,CharSequence contentTitle, CharSequence contentText,Intent intent, int icon) {
    	   NotificationManager notificationmanager = (NotificationManager) context
                   .getSystemService("notification");
           PendingIntent pendingintent = null;
           if (intent != null) {
               pendingintent = PendingIntent.getActivity(context, 0, intent, 0);
           } else {
               pendingintent = PendingIntent.getBroadcast(context, 0, new Intent(
                       "com.facehand.action.DUMMY_ACTION"), 0);
           }
           Notification notification = new Notification(icon, contentTitle,System.currentTimeMillis());
           notification.ledARGB = -16711936;
           notification.ledOnMS = 300;
           notification.ledOffMS = 1000;
           //设置声音
           notification.flags = Notification.DEFAULT_SOUND | notification.flags;
           //设置为点击清除
           notification.flags = Notification.FLAG_AUTO_CANCEL | notification.flags;
           notification.setLatestEventInfo(context, contentTitle, contentText,pendingintent);
           notificationmanager.notify(id, notification);
       }
       
       public static void notifyCancel(Context context, int id) {
       	 NotificationManager notificationmanager = (NotificationManager) context
                    .getSystemService("notification");
       	 notificationmanager.cancel(id);
       }
}