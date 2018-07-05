package com.sswdemo.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.sswdemo.base.AppParameters;

public class QHttpClient {

	private static final String TAG = "QHttpClient";
	private static final int CONNECTION_TIMEOUT = 50000;
	private LogWriter mLogWriter;
	//private HttpClient mHttpClient = null;
	public QHttpClient() {
/*		File logf = new File(AppParameters.baseDir + File.separator+ "sheshang.log");
		mLogWriter = LogWriter.open(logf.getAbsolutePath());*/

	}
 
	
	/**
	 * Using GET method.
	 * 
	 * @param url
	 *            The remote URL.
	 * @param queryString
	 *            The query string containing parameters
	 * @return Response string.
	 * @throws Exception
	 */
	
	public String httpGet(String url, String queryString) throws Exception {
		String responseData = null;

		if (queryString != null && !queryString.equals("")) {
			url += "/" + queryString;
		}
		
		
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));

		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT); // 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				CONNECTION_TIMEOUT); // 读取超时
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(CONNECTION_TIMEOUT); // 链接超时
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(CONNECTION_TIMEOUT); // 读取超时
		GetMethod httpGet = new GetMethod(url);
		httpGet.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));
		try {
			int statusCode = httpClient.executeMethod(httpGet);
			if (statusCode != HttpStatus.SC_OK) {

				throw new QNonSCOKException("" + statusCode);
			}

			
			// Read the response body.
			responseData = httpGet.getResponseBodyAsString();

		}catch(Exception e){
			LogUtil.e(TAG, e.toString());
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}

	/**
	 * 网络连接，内部不附加用户id
	 * @param url
	 * @param queryString
	 * @return
	 * @throws Exception
	 */
	public String httpGetNoUserid(String url) throws Exception {
		String responseData = null;
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));

		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT); // 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				CONNECTION_TIMEOUT); // 读取超时
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(CONNECTION_TIMEOUT); // 链接超时
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(CONNECTION_TIMEOUT); // 读取超时

		// if (null != AppParameters.cookies)
		// httpClient.getState().addCookies(AppParameters.cookies);
		LogUtil.i(TAG, "提交url："+url);
		GetMethod httpGet = new GetMethod(url);
		httpGet.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));

//		if (!AppParameters.userId.equals("")) { // 按照后台要求加的
//			httpGet.addRequestHeader("uid", AppParameters.userId);
//		}
		try {
			int statusCode = httpClient.executeMethod(httpGet);
			mLogWriter.print("===============================");
			mLogWriter.print("httpGet:" + httpGet.getURI());
			if (statusCode != HttpStatus.SC_OK) {
				mLogWriter.print("HttpStatusCode:" + statusCode);
				mLogWriter.print("ErroResponse:"
						+ httpGet.getResponseBodyAsString());
				mLogWriter.print("===============================");
				mLogWriter.print("\n");
				mLogWriter.print("\n");
				throw new QNonSCOKException("" + statusCode);
			}
			mLogWriter.print("HttpStatusCode:" + statusCode);
			mLogWriter.print("===============================");
			// Read the response body.
			responseData = httpGet.getResponseBodyAsString();

		}catch(Exception e){
			LogUtil.e(TAG, e.toString());
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}
	
	public void httpDownloadFile(String url, String queryString,
			String DestFilePath) throws Exception {
		String responseData = null;

		if (queryString != null && !queryString.equals("")) {
			url += "?" + queryString;
		}

		HttpClient httpClient = new HttpClient();

		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT); // 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				CONNECTION_TIMEOUT); // 读取超时
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(CONNECTION_TIMEOUT); // 链接超时
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(CONNECTION_TIMEOUT); // 读取超时

		if (!AppParameters.userId.equals("")) {
			if (url.indexOf("?") > -1) { // 有参数
				url = url + "&uid=" + AppParameters.userId;
			} else {
				// 无参数
				url = url + "?uid=" + AppParameters.userId;
			}
		}

		// if (null != AppParameters.cookies)
		// httpClient.getState().addCookies(AppParameters.cookies);

		File destFile;
		GetMethod httpGet = new GetMethod(url);
		httpGet.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));

		if (!AppParameters.userId.equals("")) { // 按照后台要求加的
			httpGet.addRequestHeader("uid", AppParameters.userId);
		}
		try {
			int statusCode = httpClient.executeMethod(httpGet);
			if (statusCode != HttpStatus.SC_OK) {
				throw new QNonSCOKException("" + statusCode);
			}
			// Read the response body.
			destFile = new File(DestFilePath);
			if (destFile.exists()) {
				long fileLength = httpGet.getResponseContentLength();
				if (destFile.length() == fileLength) {
					return; // 如果文件已下载过，则不做任何处理
				} else {
					if (!destFile.delete()) {
						throw new RuntimeException("删除已有文件失败，文件名："
								+ DestFilePath);
					}
				}
			}

			InputStream is = httpGet.getResponseBodyAsStream();
			OutputStream os = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) != -1) {
				os.write(buffer, 0, length);
			}

			is.close();
			os.close();

		} finally {
			httpGet.releaseConnection();
			httpClient = null;
		}

	}

	/**
	 * Using POST method.
	 * 
	 * @param url
	 *            The remote URL.
	 * @param queryString
	 *            The query string containing parameters
	 * @return Response string.
	 * @throws Exception
	 */
	public String httpPost1(String url, String queryString) throws Exception {
		String responseData = null;
		HttpClient httpClient = new HttpClient();

		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT); // 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				CONNECTION_TIMEOUT); // 读取超时
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(CONNECTION_TIMEOUT); // 链接超时
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(CONNECTION_TIMEOUT); // 读取超时

		if (!AppParameters.userId.equals("")) {
			if (url.indexOf("?") > -1) { // 有参数
				url = url + "&uid=" + AppParameters.userId;
			} else {
				// 无参数
				url = url + "?uid=" + AppParameters.userId;
			}
		}

		// if (null != AppParameters.cookies)
		// httpClient.getState().addCookies(AppParameters.cookies);
		LogUtil.i(TAG, "url="+url);
		PostMethod httpPost = new PostMethod(url);

		if (!AppParameters.userId.equals("")) { // 按照后台要求加的
			httpPost.addRequestHeader("uid", AppParameters.userId);
		}
		httpPost.addParameter("Content-Type",
				"application/x-www-form-urlencoded");
		httpPost.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));
		if (queryString != null && !queryString.equals("")) {
			httpPost.setRequestEntity(new ByteArrayRequestEntity(queryString
					.getBytes()));
		}

		try {
			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode != HttpStatus.SC_OK) {
				throw new QNonSCOKException("" + statusCode);
			}
			responseData = httpPost.getResponseBodyAsString();
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}
	
	/**
	 * Using POST method.
	 * 
	 * @param url
	 *            The remote URL.
	 * @param nameValuePairs
	 *            The query string containing parameters params.add(new
	 *            BasicNameValuePair("name","this is post"));
	 * @return Response string.
	 * @throws Exception
	 */
	public String httpPost(String url, List<NameValuePair> nameValuePairs) {
		String responseData = null;
		try {
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setHeader("http.socket.timeout", "" + CONNECTION_TIMEOUT);
			post.setHeader("User-Agent", "Android_mobile");
			HttpParams httpParameters;
			httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					CONNECTION_TIMEOUT);// optional
			HttpConnectionParams.setSoTimeout(httpParameters,
					CONNECTION_TIMEOUT);// optional
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient(httpParameters)
					.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new AppNonSCOKException("" + statusCode);
			}
			responseData = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(TAG, e.toString());
		}
		return responseData;
	}
	
	public byte[] httpPostGetWithByte(String url, String queryString) throws Exception {
		byte[] responseData = null;
		HttpClient httpClient = new HttpClient();

		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT); // 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				CONNECTION_TIMEOUT); // 读取超时
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(CONNECTION_TIMEOUT); // 链接超时
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(CONNECTION_TIMEOUT); // 读取超时

		if (!AppParameters.userId.equals("")) {
			if (url.indexOf("?") > -1) { // 有参数
				url = url + "&uid=" + AppParameters.userId;
			} else {
				// 无参数
				url = url + "?uid=" + AppParameters.userId;
			}
		}

		// if (null != AppParameters.cookies)
		// httpClient.getState().addCookies(AppParameters.cookies);
		LogUtil.i(TAG, "url="+url);
		PostMethod httpPost = new PostMethod(url);

		if (!AppParameters.userId.equals("")) { // 按照后台要求加的
			httpPost.addRequestHeader("uid", AppParameters.userId);
		}
		httpPost.addParameter("Content-Type",
				"application/x-www-form-urlencoded");
		httpPost.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));
		if (queryString != null && !queryString.equals("")) {
			httpPost.setRequestEntity(new ByteArrayRequestEntity(queryString
					.getBytes()));
		}

		try {
			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode != HttpStatus.SC_OK) {
				throw new QNonSCOKException("" + statusCode);
			}
			responseData = httpPost.getResponseBody();
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}

	/**
	 * Using POST method with multiParts.
	 * 
	 * @param url
	 *            The remote URL.
	 * @param queryString
	 *            The query string containing parameters
	 * @param files
	 *            The list of image files
	 * @return Response string.
	 * @throws Exception
	 */
	public String httpPostWithFile(String url, String queryString,
			List<QParameter> files) throws Exception {

		String responseData = null;

		if (!queryString.equals("")) {
			url += '?' + queryString;
		}
		HttpClient httpClient = new HttpClient();

		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT); // 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				CONNECTION_TIMEOUT); // 读取超时
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(CONNECTION_TIMEOUT); // 链接超时
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(CONNECTION_TIMEOUT); // 读取超时

		if (!AppParameters.userId.equals("")) {
			if (url.indexOf("?") > -1) { // 有参数
				url = url + "&uid=" + AppParameters.userId;
			} else {
				// 无参数
				url = url + "?uid=" + AppParameters.userId;
			}
		}

		// if (null != AppParameters.cookies)
		// httpClient.getState().addCookies(AppParameters.cookies);

		PostMethod httpPost = new PostMethod(url);

		if (!AppParameters.userId.equals("")) { // 按照后台要求加的
			httpPost.addRequestHeader("uid", AppParameters.userId);
		}
		try {
			List<QParameter> listParams = QHttpUtil
					.getQueryParameters(queryString);
			int length = listParams.size() + (files == null ? 0 : files.size());
			Part[] parts = new Part[length];
			int i = 0;
			for (QParameter param : listParams) {
				parts[i++] = new StringPart(param.mName,
						QHttpUtil.formParamDecode(param.mValue), "UTF-8");
			}
			for (QParameter param : files) {
				File file = new File(param.mValue);
				parts[i++] = new FilePart(param.mName, file.getName(), file,
						QHttpUtil.getContentType(file), "UTF-8");
			}

			httpPost.setRequestEntity(new MultipartRequestEntity(parts,
					httpPost.getParams()));

			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode != HttpStatus.SC_OK) {
				throw new QNonSCOKException("" + statusCode);
			}
			responseData = httpPost.getResponseBodyAsString();
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}

}
