package com.iss.upnptest.utils;

import java.util.Locale;
import org.fourthline.cling.support.model.ProtocolInfo;
import android.text.TextUtils;

/**
 * @author hubing
 * @version 1.0.0 2015-4-16
 */

public class FiletypeUtil {

	public static final int FILETYPE_MUSIC = 0;
	public static final int FILETYPE_PIC = 1;
	public static final int FILETYPE_MOVIE = 2;
	public static final int FILETYPE_OTHER = 3;

	/**
	 * 判断文件类型
	 * @param pInfo
	 * @return
	 */
	public static int getFiletype(ProtocolInfo pInfo) {
		String contentFormat = pInfo.getContentFormat();
		return getFiletype(contentFormat);
	}
	
	/**
	 * 判断文件类型
	 * @param contentFormat
	 * @return
	 */
	public static int getFiletype(String contentFormat) {
		String[] types = contentFormat.split("/");
		if (types.length >= 0) {
			if ("audio".equals(types[0])) {
				return FILETYPE_MUSIC;
			}
			if ("video".equals(types[0])) {
				return FILETYPE_MOVIE;
			}
			if ("image".equals(types[0])) {
				return FILETYPE_PIC;
			}
		}
		return FILETYPE_OTHER;
	}
	
	/**
	 * 根据文件类型获取文件的mime类型
	 * @param filetype
	 */
	public static String getMIMEType(int filetype) {
		String mimeType = "*/*";
		switch (filetype) {
		case FILETYPE_MUSIC:
			mimeType = "audio/*";
			break;
		case FILETYPE_PIC:
			mimeType = "image/*";
			break;
		case FILETYPE_MOVIE:
			mimeType = "video/*";
			break;
		}
		return mimeType;
	}
	
	/**
	 * 根据url来判断文件类型
	 * @param res
	 * @return
	 * @deprecated
	 */
	public static int getResoucesFiletype(String url) {
		if (!TextUtils.isEmpty(url)) {
			if (isMusicType(url)) {
				return FILETYPE_MUSIC;
			}
			if (isMovieType(url)) {
				return FILETYPE_MOVIE;
			}
			if (isPicType(url)) {
				return FILETYPE_PIC;
			}
		}
		return FILETYPE_OTHER;
	}
	
	/**
	 * 判断文件是否是音乐文件
	 * @param url
	 * @return
	 */
	public static boolean isMusicType(String url) {
		String mRes = url.toUpperCase(Locale.CHINA);
		if (mRes.endsWith(".MP3")) {
			return true;
		} else if (mRes.endsWith(".WMA")) {
			return true;
		} else if (mRes.endsWith(".WAV")) {
			return true;
		} else if (mRes.endsWith(".MOD")) {
			return true;
		} else if (mRes.endsWith(".RA")) {
			return true;
		} else if (mRes.endsWith(".CD")) {
			return true;
		} else if (mRes.endsWith(".MD")) {
			return true;
		} else if (mRes.endsWith(".ASF")) {
			return true;
		} else if (mRes.endsWith(".AAC")) {
			return true;
		} else if (mRes.endsWith(".Mp3Pro")) {
			return true;
		} else if (mRes.endsWith(".VQF")) {
			return true;
		} else if (mRes.endsWith(".FLAC")) {
			return true;
		} else if (mRes.endsWith(".APE")) {
			return true;
		} else if (mRes.endsWith(".MID")) {
			return true;
		} else if (mRes.endsWith(".OGG")) {
			return true;
		} else if (mRes.endsWith(".M4A")) {
			return true;
		} else if (mRes.endsWith(".AAC+")) {
			return true;
		} else if (mRes.endsWith(".AIFF")) {
			return true;
		} else if (mRes.endsWith(".AU")) {
			return true;
		} else if (mRes.endsWith(".VQF")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断文件是否是视频文件
	 * @param url
	 * @return
	 */
	public static boolean isMovieType(String url) {
		String mRes = url.toUpperCase(Locale.CHINA);
		if (mRes.endsWith(".WMV")) {
			return true;
		} else if (mRes.endsWith(".ASF")) {
			return true;
		} else if (mRes.endsWith(".ASX")) {
			return true;
		} else if (mRes.endsWith(".RM")) {
			return true;
		} else if (mRes.endsWith(".RMVB")) {
			return true;
		} else if (mRes.endsWith(".MPG")) {
			return true;
		} else if (mRes.endsWith(".MPEG")) {
			return true;
		} else if (mRes.endsWith(".MPE")) {
			return true;
		} else if (mRes.endsWith(".3GP")) {
			return true;
		} else if (mRes.endsWith(".MOV")) {
			return true;
		} else if (mRes.endsWith(".MP4")) {
			return true;
		} else if (mRes.endsWith(".MPG_PS")) {
			return true;
		} else if (mRes.endsWith(".M4V")) {
			return true;
		} else if (mRes.endsWith(".AVI")) {
			return true;
		} else if (mRes.endsWith(".DAT")) {
			return true;
		} else if (mRes.endsWith(".MKV")) {
			return true;
		} else if (mRes.endsWith(".FLV")) {
			return true;
		} else if (mRes.endsWith(".VOB")) {
			return true;
		} else if (mRes.endsWith(".WTV")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断文件是否是图片文件
	 * @param url
	 * @return
	 */
	public static boolean isPicType(String url) {
		String mRes = url.toUpperCase(Locale.CHINA);
		if (mRes.endsWith(".BMP")) {
			return true;
		} else if (mRes.endsWith(".JPG")) {
			return true;
		} else if (mRes.endsWith(".JPEG")) {
			return true;
		} else if (mRes.endsWith(".PNG")) {
			return true;
		} else if (mRes.endsWith(".GIF")) {
			return true;
		}
		return false;
	}
	
}
