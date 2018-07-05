package com.abt.ssw.servcie;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class LoginManager {

	private Context mContext;

	public LoginManager(Context mContext) {
		super();
		this.mContext = mContext;
	}

	/**
	 * save login info 
	 * @param userName
	 * @param password
	 * @param isRememberPassword
	 */
	public void login(String userName, int userId){
		SharedPreferences sp = mContext.getSharedPreferences("login", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("user_name", userName);
		editor.putInt("user_id", userId);

		editor.commit();
	}

	public void delete(){
		SharedPreferences sp = mContext.getSharedPreferences("login", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.remove("user_name");
		editor.remove("user_id");
		editor.commit();
	}


	/**
	 * get login
	 * @return
	 */
	public int getUserId(){
		SharedPreferences sp = mContext.getSharedPreferences("login", Context.MODE_PRIVATE);
		return sp.getInt("user_id", -1);
	}
}
