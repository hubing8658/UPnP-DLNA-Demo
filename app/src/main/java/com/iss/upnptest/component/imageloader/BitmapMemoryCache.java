package com.iss.upnptest.component.imageloader;

import com.iss.upnptest.utils.LogUtil;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * @author hubing
 * @version 1.0.0 2015-5-14
 */

public class BitmapMemoryCache {

	private static final String TAG = BitmapMemoryCache.class.getSimpleName();
	
	private LruCache<String, Bitmap> mCache;
	
	public BitmapMemoryCache() {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	/**
	 * 从缓存中取图片
	 * @param url
	 * @return
	 */
	public Bitmap getBitmap(String url) {
		LogUtil.d(TAG, "从缓存中取图片：" + url);
		if (url != null && mCache != null) {
			return mCache.get(url);
		}
		return null;
	}

	/**
	 * 添加图片到缓存
	 * @param url
	 * @param bitmap
	 */
	public void putBitmap(String url, Bitmap bitmap) {
		LogUtil.d(TAG, "添加图片到缓存：" + url);
		if (url != null && mCache != null) {
			mCache.put(url, bitmap);
		}
	}
	
	/**
	 * 移除缓存
	 * @param url
	 */
	public void removeBitmapCache(String url) {
		if (url != null && mCache != null) {
			Bitmap bm = mCache.remove(url);
			if (bm != null) {
				bm.recycle();
			}
		}
	}
	
	/**
	 * 清空缓存
	 */
	public void cleanCache() {
		if (mCache != null) {
			if (mCache.size() > 0) {
				LogUtil.d(TAG, "清空所有缓存数据");
				mCache.evictAll();
			}
		}
	}
	
}
