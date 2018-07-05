package com.abt.ssw.loadimage;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * 从网络获取图片
 * 
 * @author xuena.ni
 * 
 */
public class ImageGetForHttp {

	private static final String LOG_TAG = "ImageGetForHttp";
	
	// sd卡上的缓存文件夹
	private static final String CACHDIR = "掌中全房";
	// 定义缓存文件后缀
	private static final String WHOLESALE_CONV = ".cache";
	// 缓存空间大小
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	// 规定的最大缓存
	private static final int CACHE_SIZE = 10;
	// 规定1MB大小
	private static final int MB = 1024 * 1024;

	public static Bitmap downloadBitmap(String url) {
		// final int IO_BUFFER_SIZE = 4 * 1024;

		// AndroidHttpClient(2.2) 不允许在主线程中使用
		// final HttpClient client = AndroidHttpClient.newInstance("Android");
		HttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			// 请求失败
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					// return BitmapFactory.decodeStream(inputStream);
					FilterInputStream fit = new FlushedInputStream(inputStream);
					return BitmapFactory.decodeStream(fit);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
		} catch (IllegalStateException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Incorrect URL: " + url);
		} catch (Exception e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
		} finally {
			// if ((client instanceof AndroidHttpClient)) {
			// ((AndroidHttpClient) client).close();
			// }
			if ((client instanceof DefaultHttpClient)) {
				((DefaultHttpClient) client).getConnectionManager().shutdown();
			}

		}
		return null;
	}

	public static Bitmap getBitMap(String url){		
		if(getImage(url) != null)
			return getImage(url);
		Bitmap bitmap = downloadBitmap(url);
		saveBmpToSd(bitmap, url);
		return bitmap;
	}
	
	/**
	 * 根据url在图片缓存中得到图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getImage(final String url) {
		final String path = getDirectory() + "/" + convertUrlToFileName(url);
		File file = new File(path);
		if (file.exists()) {
			Bitmap bmp = BitmapFactory.decodeFile(path);
			if (bmp == null) {
				file.delete();
			} else {
				// 更新图片最后修改时间				
				return bmp;
			}
		}
		return null;
	}

	/**
	 * 将图片保存到sd卡
	 * 
	 * @param bm
	 * @param url
	 */
	public static void saveBmpToSd(Bitmap bm, String url) {
		if (bm == null) {
			// 需要保存的是一个空值
			return;
		}
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			// SD空间不足
			return;
		}
		String filename = convertUrlToFileName(url);
		String dir = getDirectory();
		File file = new File(dir + "/" + filename);
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();

		} catch (FileNotFoundException e) {
			Log.w("ImageFileCache", "FileNotFoundException");
		} catch (IOException e) {
			Log.w("ImageFileCache", "IOException");
		}
	}
	
	/**
	 * 将url转成文件名
	 * 
	 * @param url
	 * @return
	 */
	private static String convertUrlToFileName(String url) {
		String[] strs = url.split("/");
		return strs[strs.length - 1] + WHOLESALE_CONV;
	}
	
	/**
	 * 计算sdcard上的剩余空间
	 * 
	 * @return
	 */
	private static int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}
	
	
	/**
	 * 获得缓存目录
	 * 
	 * @return
	 */
	private static String getDirectory() {
		String dir = getSDPath() + "/" + CACHDIR;
		String substr = dir.substring(0, 4);
		if (substr.equals("/mnt")) {
			dir = dir.replace("/mnt", "");
		}
		return dir;
	}

	/**
	 * 取SD卡路径
	 * 
	 * @return
	 */
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		if (sdDir != null) {
			return sdDir.toString();
		} else {
			return "";
		}
	}
	
	/*
	 * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */
	static class FlushedInputStream extends FilterInputStream {

		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

}
