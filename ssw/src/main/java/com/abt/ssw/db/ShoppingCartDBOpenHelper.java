package com.abt.ssw.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class ShoppingCartDBOpenHelper extends SQLiteOpenHelper {
	
	public static final String NAME = "shopping_cart.db";
	private static final int VERSION = 1;
	

	public ShoppingCartDBOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
	}
  
	@Override
	public void onCreate(SQLiteDatabase db) {
		//action db
		String sql = "CREATE TABLE IF NOT EXISTS shop (" +
				"shop_id varchar(20)  primary key" +
				",shop_name varchar(20)" +
				")";
			 
		db.execSQL(sql); 
		
		//top db
		String sql1 = "CREATE TABLE IF NOT EXISTS product (" +
				"shop_id varchar(20)" +
				",product_id varchar(20) primary key" +
				",icon varchar(50)" +
				",title varchar(20)" +
				",price varchar(20)" +
				",count INTEGER" +
				",description varchar(20)" +
				")";
		db.execSQL(sql1); 


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS shop");
		db.execSQL("DROP TABLE IF EXISTS product");
		onCreate(db);
	}

}
