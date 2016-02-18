
package com.iss.upnptest.utils;

import java.io.File;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class SdcardUtil {

    /**
     * SD卡是否可写
     * 
     * @return
     */
    public static boolean isSdcardWritable() {

        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states,
            // but all we need
            // to know is we can neither read nor write
            mExternalStorageWriteable = false;
        }

        return mExternalStorageWriteable;
    }

    /**
     * SD卡是否可读
     * 
     * @return
     */
    public static boolean isSdcardReadable() {

        boolean mExternalStorageAvailable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
        } else {
            // Something else is wrong. It may be one of many other states,
            // but all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = false;
        }

        return mExternalStorageAvailable;
    }

    /**
     * 获取sd卡可用空间
     * 
     * @return
     */
    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getSdcardAvailableSpace() {

        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long blockSize;
        if (Build.VERSION.SDK_INT >= 18) {
        	blockSize = statFs.getBlockSizeLong();
        } else {
        	blockSize = statFs.getBlockSize();
        }
        long avaliableBlocks = statFs.getAvailableBlocksLong();

        long availableSpace = avaliableBlocks * 1l * blockSize;

        return availableSpace;
    }

    /**
     * 获取目录所占空间
     * 
     * @return
     */
    public static long getDirOccupiedSpace(String dirPath) {

        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return 0l;
        }

        long dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            dirSize += files[i].length();
        }

        return dirSize;
    }

}
