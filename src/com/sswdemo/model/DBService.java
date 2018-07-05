package com.sswdemo.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sswdemo.model.ChatMsgEntity;

public class DBService {

	private DBOpenHelper dbHelper;
	private SQLiteDatabase db;

	public DBService(Context context) {
		dbHelper = new DBOpenHelper(context);
	}

	public void insertEntity(ChatMsgEntity entity) {
		db = dbHelper.getWritableDatabase();
		Log.d("MILA", "entity.getTitle():　" + entity.getTitle());
		Log.d("MILA", "entity.getTime():　" + entity.getTime());
		Log.d("MILA", "entity.getMsg():　" + entity.getMsg());
		Log.d("MILA", "entity.getType():　" + entity.getType());
		try {
			ContentValues cv = new ContentValues();
			cv.put("title", entity.getTitle());
			cv.put("time", entity.getTime());
			cv.put("msg", entity.getMsg());
			cv.put("type", entity.getType());
			cv.put("img", 0);
			db.insert("msglist", null, cv);
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	public void updateEntity(ChatMsgEntity entity) throws Exception {
		db = dbHelper.getWritableDatabase();
		try {
			ContentValues cv = new ContentValues();
			cv.put("id", entity.getId());
			cv.put("title", entity.getTitle());
			cv.put("time", entity.getTime());
			cv.put("msg", entity.getMsg());
			cv.put("type", entity.getType());
			db.update("msglist", cv, "_id = ?", new String[] { entity.getId()
					+ "" });
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	public ChatMsgEntity getEntityById(int id) throws Exception {
		if (id < 0) {
			return null;
		}
		ChatMsgEntity entity = new ChatMsgEntity();
		db = dbHelper.getWritableDatabase();
		String sql = "SELECT id, title, time,"
				+ "msg, type FROM updates " + "WHERE id = ?";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, new String[] { id + "" });
			while (cursor.moveToNext()) {
				entity.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				entity.setTitle((cursor.getString(cursor
						.getColumnIndex("title"))));
				entity.setTime((cursor.getString(cursor
						.getColumnIndex("time"))));
				entity.setMsg((cursor.getString(cursor
						.getColumnIndex("msg"))));
				entity.setType(cursor.getInt((cursor.getColumnIndex("type"))));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return entity;
	}

	public static String getComparedFormateDateString(long time) {
		String DATE_FORMAT_PATTERN1 = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN1);
		String dStr = sdf.format(new Date(time));
		return dStr;
	}

}
