package com.sswdemo.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String NAME = "ssw.db";
	private static final int VERSION = 1;

	public DBOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("MSG_LIST", "onCreate()");
		String sql = "CREATE TABLE IF NOT EXISTS msglist("
				+ "id integer primary key autoincrement,"
				+ "title varchar(32), " 
				+ "msg varchar(1280), " 
				+ "time varchar(32), "
				+ "img integer, "
				+ "type integer)";
		db.execSQL(sql);
		initDatabase(db);
	}
	
	private void initDatabase(SQLiteDatabase db) {
			for (int i = 0; i < 10; i++) {
				db.execSQL("insert into msglist(title, msg, time, type)values('小区物业"
						+ i + "', 'zzz最新消息最新消息最新消息最新消息最新消息zzz', '09:0" + i + "', '1')");
				}
	}
	
	public boolean isTableExist(String tableName, SQLiteDatabase db) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		try {
			String sql = "select count(*) as c from sqlite_master where type = 'table' "
					+ "and name = '" + tableName.trim() + "' ";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				Log.d("MSG_LIST", "count: " + count);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("MSG_LIST", "onUpgrade()");
		db.execSQL("DROP TABLE IF EXISTS msglist");
		onCreate(db);
	}
}
