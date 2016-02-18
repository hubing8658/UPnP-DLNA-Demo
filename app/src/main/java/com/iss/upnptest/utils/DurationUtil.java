package com.iss.upnptest.utils;

/**
 * @author hubing
 * @version 1.0.0 2015-5-12
 */

public class DurationUtil {

	public static String toMilliTimeString(long milliseconds) {
		milliseconds = milliseconds / 1000;
		return toTimeString(milliseconds);
		
	}
	
	public static String toTimeString(long seconds) {
		long hours = seconds / (60 * 60);
		long minutes = seconds % (60 * 60) / 60;
		long second = seconds % 60;
		return ((hours < 10 ? "0" : "") + hours
                + ":" + (minutes < 10 ? "0" : "") + minutes
                + ":" + (second < 10 ? "0" : "") + second);
	}
	
}

