package com.iss.upnptest.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author hubing
 * @version 1.0.0 2015-5-20
 */

public class ToastUtil {

	public static void showLongToast(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}
	
	public static void showLongToast(Context ctx, int resId) {
		Toast.makeText(ctx, resId, Toast.LENGTH_LONG).show();
	}
	
	public static void showShortToast(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void showShortToast(Context ctx, int resId) {
		Toast.makeText(ctx, resId, Toast.LENGTH_SHORT).show();
	}
	
}

