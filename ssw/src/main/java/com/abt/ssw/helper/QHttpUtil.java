package com.abt.ssw.helper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class QHttpUtil {
	/**
	 * Return the MIME type based on the specified file name.
	 * 
	 * @param fileName
	 *            path of input file.
	 * @return MIME type.
	 */
	public static String getContentType(String fileName) {
		// return new MimetypesFileTypeMap().getContentType(fileName);
		return null;
	}

	/**
	 * Return the MIME type based on the specified file name.
	 * 
	 * @param file
	 *            File
	 * @return MIME type.
	 */
	public static String getContentType(File file) {
		// return new MimetypesFileTypeMap().getContentType(file);
		return null;
	}

	/**
	 * Return the list of query parameters based on the specified query string.
	 * 
	 * @param queryString
	 * @return the list of query parameters.
	 */
	public static List<QParameter> getQueryParameters(String queryString) {
		if (queryString.startsWith("?")) {
			queryString = queryString.substring(1);
		}

		List<QParameter> result = new ArrayList<QParameter>();

		if (queryString != null && !queryString.equals("")) {
			String[] p = queryString.split("&");
			for (String s : p) {
				if (s != null && !s.equals("")) {
					if (s.indexOf('=') > -1) {
						String[] temp = s.split("=");
						result.add(new QParameter(temp[0], temp[1]));
					}
				}
			}
		}

		return result;
	}

	/**
	 * Convert %XX
	 * 
	 * @param value
	 * @return
	 */
	public static String formParamDecode(String value) {
		int nCount = 0;
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '%') {
				i += 2;
			}
			nCount++;
		}

		byte[] sb = new byte[nCount];

		for (int i = 0, index = 0; i < value.length(); i++) {
			if (value.charAt(i) != '%') {
				sb[index++] = (byte) value.charAt(i);
			} else {
				StringBuilder sChar = new StringBuilder();
				sChar.append(value.charAt(i + 1));
				sChar.append(value.charAt(i + 2));
				sb[index++] = Integer.valueOf(sChar.toString(), 16).byteValue();
				i += 2;
			}
		}
		String decode = "";
		try {
			decode = new String(sb, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decode;
	}

	/**
	 * �ж��ַ��Ƿ�Ϊ��
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (null == str || str.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �ָ�ؽ��
	 * 
	 * @param response
	 *            �����ַ�
	 * @return
	 */
	public static Map<String, String> splitResponse(String response) {
		// ���淵�ؽ��
		Map<String, String> map = new HashMap<String, String>();
		// �ж��Ƿ�Ϊ��
		if (!QHttpUtil.isEmpty(response)) {
			// �ѡ�&�����зָ�
			String[] array = response.split("&");
			if (array.length > 2) {
				String tokenStr = array[0]; // oauth_token=xxxxx
				String secretStr = array[1];// oauth_token_secret=xxxxxxx
				String[] token = tokenStr.split("=");
				if (token.length == 2) {
					map.put("oauth_token", token[1]);
				}
				String[] secret = secretStr.split("=");
				if (secret.length == 2) {
					map.put("oauth_token_secret", secret[1]);
				}
			}
		}
		return map;
	}

	/**
	 * 判断网络是否连接
	 * @param context 
	 * @return true 表示已经连接, 否则为false。
	 */
	public static boolean checkNetWork(Context context) {
		ConnectivityManager connectivityManager = 
				(ConnectivityManager)context.getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}
}
