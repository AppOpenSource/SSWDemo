package com.abt.ssw.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.abt.ssw.activitys.R;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;

public class HttpTools {
	public static final int METHOD_GET = 1;
	public static final int METHOD_POST = 2;
	public static  File sd_path =null;
	/** 执行downfile后，得到下载文件的大小 */
	private long contentLength;
	/** 返回连接失败信息 **/
	private String strResult = "服务器无法连接，请检查网络";

	/** http 请求头参数 **/
	private HttpParams httpParams;
	/** httpClient 对象 **/
	private DefaultHttpClient httpClient;

	/** 得到上下文 **/
	private Context context;
	private Activity activity = null;
	
	/**更新界面UI**/
	Handler mHandler;

	/** HTTP帮助类的构造方法 */
	public HttpTools(Context context) {
		this.context = context;
		
		getHttpClient();
	}
	
	public HttpTools() {}
	

	/**
	 * 得到 apache http HttpClient对象 一般情况下，我们获取httpclient对象就一句话，httpClient = new
	 * DefaultHttpClient();
	 * 但是这个DefaultHttpClient()方法中没传参数，少了许多参数限制，所以就下面这个方法，通过设置 HTTP参数来约束请求
	 * **/
	public DefaultHttpClient getHttpClient() {
		/** 创建 HttpParams 以用来设置 HTTP 参数 **/
		httpParams = new BasicHttpParams();

		/** 设置连接超时和 Socket 超时，以及 Socket 缓存大小 **/

		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);

		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);

		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		// 以新的方式重新访问，这里就是通过参数设置来访问
		/** 这里就是HTTP访问重定向的问题了，true的意思就是让访问重定向，get和post重定向设置相同 */
		HttpClientParams.setRedirecting(httpParams, true);

		/**
		 * 创建一个 HttpClient 实例 //增加自动选择网络，自适应cmwap、CMNET、wifi或3G
		 */
		MyHttpCookies li = new MyHttpCookies(context);
		// 可以理解为网络代理商
		String proxyStr = li.getHttpProxyStr();
		if (proxyStr != null && proxyStr.trim().length() > 0) {
			HttpHost proxy = new HttpHost(proxyStr, 80);
			httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
					proxy);
		}
		/** 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient **/
		httpClient = new DefaultHttpClient(httpParams);
		// 设置请求异常时需要做出的处理 .为了开启自定义异常恢复机制，应该提供一个HttpRequestRetryHandler接口的实现。
		httpClient.setHttpRequestRetryHandler(requestRetryHandler);

		return httpClient;

	}

	/**
	 * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// 自定义的恢复策略
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			// 设置恢复策略，在发生异常时候将自动重试N次
			if (executionCount >= 3) {
				// 如果超过最大重试次数，那么就不要继续了
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// 如果服务器丢掉了连接，那么就重试
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// 不要重试SSL握手异常
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// 如果请求被认为是幂等的，那么就重试
				return true;
			}
			return false;
		}
	};

	/**
	 * 
	 * 总的来说DoGet是不安全的，会把用户的信息暴露在URL中，但是在手机中酷似看不到，如果有人进行网络抓包的话，会抓到。
	 * 提供GET形式的访问网络请求 doGet 参数示例：
	 * 
	 * @param url
	 *            请求地址
	 * @return 返回 String jsonResult;
	 * 
	 */
	public String doGet(String url) {
		/** 创建HttpGet对象 **/
		HttpGet httpRequest = new HttpGet(url);
		httpRequest.setHeaders(this.getHeader());
		try {
			/** 保持会话Session **/
			/** 设置Cookie **/
			MyHttpCookies li = new MyHttpCookies(context);
			CookieStore cs = li.getuCookie();
			/** 第一次请求App保存的Cookie为空，所以什么也不做，只有当APP的Cookie不为空的时。把请请求的Cooke放进去 **/
			if (cs != null) {
				httpClient.setCookieStore(li.getuCookie());
			}

			/** 保持会话Session end **/

			/* 发送请求并等待响应 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				strResult = EntityUtils.toString(httpResponse.getEntity());

				/** 执行成功之后得到 **/
				/** 成功之后把返回成功的Cookis保存APP中 **/
				// 请求成功之后，每次都设置Cookis。保证每次请求都是最新的Cookis
				li.setuCookie(httpClient.getCookieStore());

			} else {
				strResult = "Error Response: "
						+ httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = nullToString(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			strResult = nullToString(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			strResult = nullToString(e.getMessage());
			e.printStackTrace();
		} finally {
			httpRequest.abort();
			this.shutDownClient();
		}
		return strResult;
	}

	/**
	 * 提供GET形式的访问网络请求 doGet 参数示例： Map params=new HashMap();
	 * params.put("usename","helijun"); params.put("password","123456");
	 * httpClient.doGet(url,params)；
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 返回 String jsonResult;
	 * 
	 * **/
	public String doGet(String url, Map params) {
		/** 建立HTTPGet对象 **/
		String paramStr = "";
		if (params == null)
			params = new HashMap();
		/** 迭代请求参数集合 **/
		Iterator iter = params.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			String val = nullToString(entry.getValue());
			paramStr += paramStr = "&" + key + "=" + URLEncoder.encode(val);
		}
		if (!paramStr.equals("")) {
			paramStr = paramStr.replaceFirst("&", "?");
			url += paramStr;
		}
		return doGet(url);
	}
	
	/**
	 * 提供GET形式的访问网络请求 doGet 参数示例： Map params=new HashMap();
	 * params.put("usename","gongshuanglin"); params.put("password","123456");
	 * httpClient.doGet(url,params)；
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 返回 String jsonResult;
	 * 
	 */
	public String doGet(String url, List<NameValuePair> params) {
		/** 建立HTTPGet对象 **/
		String paramStr = "";
		if (params == null)
			params = new ArrayList<NameValuePair>();
		/** 迭代请求参数集合 **/

		for (NameValuePair obj : params) {
			paramStr += paramStr = "&" + obj.getName() + "="
					+ URLEncoder.encode(obj.getValue());
		}
		if (!paramStr.equals("")) {
			paramStr = paramStr.replaceFirst("&", "?");
			url += paramStr;
		}
		return doGet(url);
	}

	/**
	 * 提供Post形式的访问网络请求 Post 参数示例： doPost 参数示例 List<NameValuePair> paramlist =
	 * new ArrayList<NameValuePair>(); paramlist(new BasicNameValuePair("email",
	 * "xxx@123.com")); paramlist(new BasicNameValuePair("address", "123abc"));
	 * httpClient.doPost(url,paramlist);
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 返回 String jsonResult;
	 * **/

	public String doPost(String url, List<NameValuePair> params) {
		/* 建立HTTPPost对象 */

		HttpPost httpRequest = new HttpPost(url);
		// 设置请求Header信息、
		httpRequest.setHeaders(this.getHeader());
		try {

			/** 添加请求参数到请求对象 */
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			/** 保持会话Session **/
			/** 设置Cookie **/
			MyHttpCookies li = new MyHttpCookies(context);
			CookieStore cs = li.getuCookie();
			/** 第一次请求App保存的Cookie为空，所以什么也不做，只有当APP的Cookie不为空的时。把请请求的Cooke放进去 **/
			if (cs != null) {
				httpClient.setCookieStore(li.getuCookie());
			}

			/** 保持会话Session end **/

			/** 发送请求并等待响应 */

			HttpResponse httpResponse = httpClient.execute(httpRequest);

			/** 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				strResult = EntityUtils.toString(httpResponse.getEntity());

				/** 执行成功之后得到 **/
				/** 成功之后把返回成功的Cookis保存APP中 **/
				// 请求成功之后，每次都设置Cookis。保证每次请求都是最新的Cookis
				li.setuCookie(httpClient.getCookieStore());

				/** 设置Cookie end **/
			} else {
				strResult = "Error Response: "
						+ httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = "";
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (IOException e) {
			strResult = "";
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (Exception e) {
			strResult = "";
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} finally {
			httpRequest.abort();
			this.shutDownClient();
		}
		return strResult;
	}

	/**
	 * 通过指定的接口获取网络实体文件，并得到实体文件的大小
	 * 一般情况下当向某个接口请求实体数据时，需要客户端提供一些信息，比如用户名啦，文件名称啦之类的，并且还需要提供请求方法类型.
	 * @param url
	 * @return
	 */
	public HttpEntity DownLoadFile(String url,ArrayList<NameValuePair> params, int method) {
		/** 创建HttpGet对象 **/
		HttpUriRequest httpRequest = null;
		switch (method) {
		case METHOD_GET:
			String paramStr = "";
			if (params == null)
				params = new ArrayList<NameValuePair>();
			/** 迭代请求参数集合 **/

			for (NameValuePair obj : params) {
				paramStr += paramStr = "&" + obj.getName() + "="
						+ URLEncoder.encode(obj.getValue());
			}
			if (!paramStr.equals("")) {
				paramStr = paramStr.replaceFirst("&", "?");
				url += paramStr;
			}
			httpRequest = new HttpGet(url);
			httpRequest.setHeaders(this.getHeader());
			break;

		case METHOD_POST:
			httpRequest = new HttpPost(url);
			httpRequest.setHeaders(this.getHeader());
			if (params != null && !params.isEmpty()) {
				//设置请求实体
				try {
					UrlEncodedFormEntity requestentity = new UrlEncodedFormEntity(
							params);
					((HttpPost)httpRequest).setEntity(requestentity);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		}
		
		try {
			/** 保持会话Session **/
			/** 设置Cookie **/
			MyHttpCookies li = new MyHttpCookies(context);
			CookieStore cs = li.getuCookie();
			/** 第一次请求App保存的Cookie为空，所以什么也不做，只有当APP的Cookie不为空的时。把请请求的Cooke放进去 **/
			if (cs != null) {
				httpClient.setCookieStore(li.getuCookie());
			}
			/** 保持会话Session end **/
			/* 发送请求并等待响应 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/** 执行成功之后得到 **/
				/** 成功之后把返回成功的Cookis保存APP中 **/
				// 请求成功之后，每次都设置Cookis。保证每次请求都是最新的Cookis
				li.setuCookie(httpClient.getCookieStore());
				this.contentLength = httpResponse.getEntity()
						.getContentLength();
				/* 读返回数据 */
				return httpResponse.getEntity();
			} else {
				strResult = "Error Response: "
						+ httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (IOException e) {
			strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (Exception e) {
			strResult = e.getMessage().toString();
			e.printStackTrace();
		} finally {
			// httpRequest.abort();
			// this.shutDownClient();
		}
		this.contentLength = 0;
		return null;
	}
	/**
	 * 将下载得到的entity转化为输入流
	 * @param url
	 * @param params
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public  InputStream getStream(String url,ArrayList<NameValuePair> params,int method) throws Exception{
		InputStream in = null;
		HttpEntity _entity  = DownLoadFile(url, params, method);
		if(_entity != null){
			in = _entity.getContent();
		}
		return in;
	}
	
	/**
	 * 通过实体对象获取输入流
	 * @param entity
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static InputStream getStream(HttpEntity entity) throws IllegalStateException, IOException{
		if(entity != null){
			return entity.getContent();
		}
		return null;
	}
	
	/**
	 * 将下载得到的entity转化为字节数组，适合于图片的获取
	 * @param url
	 * @param params
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public  byte[] getByte(String url,ArrayList<NameValuePair> params,int method) throws Exception{
		byte[] _bytes = null;
		HttpEntity _entity = DownLoadFile(url, params, method);
		if(_entity != null){
			return _bytes = EntityUtils.toByteArray(_entity);
		}
		return _bytes;			
	}
	/**
	 * 将下载得到的entity转化为String类型，适合于获取服务器的JSON字符串或者返回值
	 * @param url
	 * @param params
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public  String toString(String url,ArrayList<NameValuePair> params,int method) throws Exception{
		HttpEntity _entity = DownLoadFile(url, params, method);
		if(_entity != null){
			return EntityUtils.toString(_entity, "utf-8");
		}
		return null;
	}
	
	public long getContentLength() {
		return contentLength;
	}
	
	/**
	 * 这个方式是利用HttpURLConnection进行数据请求的，但是这种方式比较原始，可扩展性没有HttPClient方便
	 * 在一般的网络请求数据源中这种方法也常用
	 * @param urlPath
	 * @return
	 */
	public String getStringByURLConnection(String urlPath) {
		String json = null;
		try {
			
			URL url = new URL(urlPath);
			// 利用HttpURLConnection对象,我们可以从网络中获取网页数据.
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 // 单位是毫秒，设置超时时间为5秒
			conn.setConnectTimeout(5*1000);
			// HttpURLConnection是通过HTTP协议请求path路径的，所以需要设置请求方式,可以不设置，因为默认为GET
			conn.setRequestMethod("GET");
			if(conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				byte[] data = readStream(is); //将输入流转化为字符数组
				json = new String(data);//把字符数组转化为字符串
			}				
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return json;
	}
	
	/**
	 * 将输入流转化为字符数组
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public  byte[] readStream(InputStream is)  {
		ByteArrayOutputStream bout = null;
		try {
			bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = is.read(buffer)) != -1){
				bout.write(buffer, 0, len);
			}
			
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bout.toByteArray();
	}
	

	/** 得到设备信息、系统版本、驱动类型 **/
	private Header[] getHeader() {
		/** 请求头信息 end **/
		MyHttpCookies li = new MyHttpCookies(context);
		return li.getHttpHeader();
	}

	/**
	 * 假如obj对象 是null返回""
	 * 
	 * @param obj
	 * @return
	 */
	public static String nullToString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	/** 销毁HTTPCLient **/
	public void shutDownClient() {
		httpClient.getConnectionManager().shutdown();
	}
	/**
	 * 下载图片文件
	 * 
	 * @author shilinw
	 * @date 2012-4-2 上午10:36:58
	 * @param ctx
	 *            上下文
	 * @param url
	 *            图片URL
	 * @return bitmap
	 * @throws IOException
	 */
	public static Bitmap bitmapFromCache(Context ctx, String url)
			throws IOException, Error {
		if (url == null || url.equals(""))
			return null;

		String urlPath = "";

		urlPath = url;
         
		// File cacheFile = ctx.getCacheDir();
		File filesDir = ctx.getFilesDir();	
		if (Utils.isSdcardReadable()) {
			filesDir = new File(Environment.getExternalStorageDirectory() + "/"
					+ ctx.getString(R.string.app_name));	
			if (!filesDir.exists()) {
				filesDir.mkdirs();
			}
		}
		File file = new File(filesDir, md5(url));
		sd_path=file;
		try {
			if (!file.exists()) {
				FileOutputStream outStream = new FileOutputStream(file);
				HttpURLConnection conn = (HttpURLConnection) new URL(urlPath)
						.openConnection();
				conn.setConnectTimeout(30 * 1000);
				conn.setRequestMethod("GET");
				if (conn.getResponseCode() == 200) {
					InputStream inStream = conn.getInputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}

					outStream.close();
					inStream.close();
				} else
					return null;
			}

			return BitmapFactory.decodeFile(file.getAbsolutePath());
		} catch (Exception ex) {
			if (ex != null)
				ex.printStackTrace();
		} catch (Error e) {
		} finally {
			if (file.length() < 10) {
				file.delete();
				return null;
			}
		}
		return null;
	}
	public final static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	public static Bitmap returnBitMap(String url){
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
		myFileUrl = new URL(url);
		} 
		catch (MalformedURLException e) {
		e.printStackTrace();
		}
		try {
		HttpURLConnection conn = (HttpURLConnection) myFileUrl
		.openConnection();
		conn.setDoInput(true);
		conn.connect();
		InputStream is = conn.getInputStream();
		bitmap = BitmapFactory.decodeStream(is);
		is.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
		return bitmap;
		
	}
		
	
	/***************************************************
	 * 检查网络连接
	 * @param context 上下文
	 * @return true 网络正常 fasle 无网络.并弹出Dialog提示设置网络
	 */
	public static  boolean checkNetwork(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			/*AlertDialog.Builder builder = new Builder(context);
			  builder.setMessage("非常抱歉，您尚未链接网络");
			  builder.setTitle("提示");
			  builder.setPositiveButton("确认", new OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			   }
			  });
			final Dialog dialog = builder.create();
			if(context instanceof Activity)
				if(!((Activity)context).isFinishing()){
					new Handler(context.getMainLooper()).post(new Runnable() {
						public void run() {
							dialog.show();
						}
					});
				}*/		

			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}

}
