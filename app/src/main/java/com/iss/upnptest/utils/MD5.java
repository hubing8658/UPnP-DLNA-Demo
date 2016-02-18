package com.iss.upnptest.utils;

/**
 * 对字符串加密
 * 
 * @version  [1.0.0.0, 2012-7-27]
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	private static final String TAG = "MD5";
	
	public static String getMD5Str(String strSrc) {
		MessageDigest md = null;
		String md5Str = null;

		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(bt);
			md5Str = HexString.bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			LogUtil.e(TAG, e, e.getMessage());
		}
		return md5Str;
	}
	
	/**
	 * 
	 * 获取16位的md5字符串值
	 * @param strSrc
	 * @return
	 */
	public String get16MD5Str(String strSrc){
		String md5Str32 = getMD5Str(strSrc);
		
		if(null != md5Str32){
			return md5Str32.substring(16);
		}
		
		return null;
	}

}
