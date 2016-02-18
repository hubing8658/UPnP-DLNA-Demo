package com.iss.upnptest.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/**
 * @author hubing
 * @version 1.0.0 2015-5-19
 */

public class BitmapUtil {

	/**
	 * 计算图片缩放值
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}
		return inSampleSize;
	}

	/**
	 * 计算图片压缩Options
	 * @param bm
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static BitmapFactory.Options calculateOptions(Bitmap bm, int reqWidth, int reqHeight) {
		Options op = new Options();
		op.outHeight = bm.getHeight();
		op.outWidth = bm.getWidth();
		op.inJustDecodeBounds = true;
		op.inSampleSize = calculateInSampleSize(op, reqWidth, reqHeight);
		op.inJustDecodeBounds = false;
		return op;
	}
	
}
