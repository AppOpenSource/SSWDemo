package com.sswdemo.loadimage;


import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * 内存中的缓存
 * 
 * @author xuena.ni
 * 
 */
public class ImageMemoryCache {

	private static final int HARD_CACHE_CAPACITY = 30;
	private HashMap<String, Bitmap> mHardBitmapCache;
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			HARD_CACHE_CAPACITY / 2);

	public ImageMemoryCache() {
		// 按从近期访问最少到近期访问最多的顺序（即访问顺序）来保存元素。排序模式accessOrder，该属性为boolean型变量，对于访问顺序，为true。
		mHardBitmapCache = new LinkedHashMap<String, Bitmap>(
				HARD_CACHE_CAPACITY / 2, 0.75f, true) {

			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(
					LinkedHashMap.Entry<String, Bitmap> eldest) {
				if (size() > HARD_CACHE_CAPACITY) {
					// Entries push-out of hard reference cache are transferred
					// to soft reference cache
					mSoftBitmapCache.put(eldest.getKey(),
							new SoftReference<Bitmap>(eldest.getValue()));
					return true;
				} else
					return false;
			}
		};

	}

	/**
	 * 从缓存中获取图片
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromCache(String url) {
		if (url == null) {
//			Log.e("ImageMemoryCache", "传入的图片地址为null");
			return null;
		}
		// 先从mHardBitmapCache缓存中获取
		synchronized (mHardBitmapCache) {
			final Bitmap bitmap = mHardBitmapCache.get(url);
			if (bitmap != null) {
				// 如果找到的话，把元素移到linkedhashmap的最前面，从而保证在LRU算法中是最后被删除
				mHardBitmapCache.remove(url);
				mHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}
		// 如果mHardBitmapCache中找不到，到mSoftBitmapCache中找
		SoftReference<Bitmap> bitmapReference = mSoftBitmapCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				// 将图片移回硬缓存
				mHardBitmapCache.put(url, bitmap);
				mSoftBitmapCache.remove(url);
				return bitmap;
			} else {
				mSoftBitmapCache.remove(url);
			}
		}
		return null;
	}

	/**
	 * 添加图片到缓存
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void addBitmapToCache(String url, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (mHardBitmapCache) {
				mHardBitmapCache.put(url, bitmap);
			}
		}
	}
}
