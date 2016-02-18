package com.iss.upnptest.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @author hubing
 * @version 1.0.0 2015-5-8
 */

public class IPUtil {

	/**
	 * 获取本机ip
	 * @param context
	 * @return
	 * @throws UnknownHostException
	 */
	public static InetAddress getLocalIpAddress(Context context) throws UnknownHostException {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return InetAddress.getByName(String.format(Locale.getDefault(), "%d.%d.%d.%d",
				(ipAddress & 0xff), 
				(ipAddress >> 8 & 0xff),
				(ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff)));
	}
	
}

