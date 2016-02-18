package com.iss.upnptest.component.imageloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import com.iss.upnptest.utils.LogUtil;
import com.iss.upnptest.utils.MD5;
import com.iss.upnptest.utils.SdcardUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * @version 1.0.0 2015-5-14
 */

public class ImageFileCache {

	private static final String TAG = ImageFileCache.class.getSimpleName();
	
	/** 文件缓存存放目录 */
    private static String CACHE_DIR = Environment.getExternalStorageDirectory().getPath() + "/iss/app_name/img/";

    // init config params
    static {
        InputStream is = null;
        try {
            Properties p = new Properties();
            is = ImageFileCache.class.getResourceAsStream("/assets/upnp.properties");
            p.load(is);

            Log.d(TAG, "load config: " + p.toString());

            String cachedir = p.getProperty("ua.imagecache.cachdir");

            if (!TextUtils.isEmpty(cachedir)) {
                CACHE_DIR = Environment.getExternalStorageDirectory().getPath() + "/" + cachedir;
            }

            Log.d(TAG, "CACHE_DIR = " + CACHE_DIR);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }

    /** 文件缓存后辍名 */
    private static final String CACHE_FILE_SUFFIX = ".cach";

    /** 最大允许的缓存空间 */
    private static final long MAX_CACHE_SPACE = 10 * 1024 * 1024;

    /** 最小的SD卡剩余空间（预留给用户做其它的事情） */
    private static final long MIN_SDCARD_AVAILABLE_SPACE = 32 * 1024 * 1024;

    /** 每次清除数据的量，即所占缓存文件的百分比 */
    private static final float CACHE_REMOVE_FACTOR = 0.4f;

    public ImageFileCache() {
        // 清理文件缓存
        removeCache();
    }

    /**
     * 从文件获取图片数据
     * 
     * @param url 图片url
     * @return 图片数据
     */
    public Bitmap getImgData(String url) {
        final String path = CACHE_DIR + convertUrlToFileName(url);
        File file = new File(path);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap == null) {
                LogUtil.w(TAG, "将文件转换成Bitmap失败，删除此文件，path = ", path);
                file.delete();
            } else {
                updateFileTime(path);
                LogUtil.d(TAG, "从文件获取Bitmap成功，path = ", path);
                return bitmap;
            }
        }
        return null;
    }

    /**
     * 将图片数据保存到sdcard
     * 
     * @param bitmap 图片数据
     * @param url 图片网址
     */
    public void saveBitmapToSdcard(final Bitmap bitmap, final String url) {
    	new Thread() {
    		public void run() {
    			if (bitmap == null) {
    				// 需要保存的是一个空值
    				LogUtil.w(TAG, "Bitmap为null，持久化存储失败，url = ", url);
    				return;
    			}
    			
    			long availableSpace = SdcardUtil.getSdcardAvailableSpace();
    			// 判断sdcard上的空间
    			if (availableSpace < MIN_SDCARD_AVAILABLE_SPACE) {
    				// SD空间不足
    				LogUtil.w(TAG, "SD卡空间不足，不做持持久化存储，availableSpace = " + availableSpace, ", url = ", url);
    				return;
    			}
    			
    			File dir = new File(CACHE_DIR);
    			if (!dir.exists()) {
    				dir.mkdirs();
    			}
    			
    			String filename = CACHE_DIR + convertUrlToFileName(url);
    			File file = new File(filename);
    			try {
    				OutputStream os = null;
    				try {
    					os = new FileOutputStream(file);
    					bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
    					os.flush();
    					
    					LogUtil.d(TAG, "持持久化存储成功，path = ", filename, ", url = ", url);
    					
    				} finally {
    					os.close();
    				}
    			} catch (Exception e) {
    				LogUtil.e(TAG, e, e.getMessage());
    			}
    			
    		}
    	}.start();
    }

    /**
     * 清理缓存。 计算存储目录下的文件大小， 当文件总大小大于规定的最大缓存大小或者sdcard剩余空间小于最小SD卡可用空间的规定时， 删除特定数量的最近没有被使用的文件
     * 
     * @return
     */
    private boolean removeCache() {

        if (!SdcardUtil.isSdcardWritable()) {
            LogUtil.w(TAG, "sdcard不可写，清理缓存失败。");
            return false;
        }

        File dir = new File(CACHE_DIR);
        File[] files = dir.listFiles();
        if (files == null) {
            LogUtil.d(TAG, "缓存数据不存在，无需清理。");
            return true;
        }

        long dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith(CACHE_FILE_SUFFIX)) {
                dirSize += files[i].length();
            }
        }

        long availableSpace = SdcardUtil.getSdcardAvailableSpace();

        LogUtil.d(TAG, "缓存文件的已有总大小为：" + dirSize, "，SD卡可用空间为：" + availableSpace);

        if (dirSize > MAX_CACHE_SPACE || availableSpace < MIN_SDCARD_AVAILABLE_SPACE) {

            // 计算出需要删除的文件的数量
            int removeNum = (int) ((CACHE_REMOVE_FACTOR * files.length) + 1);

            Arrays.sort(files, new FileLastModifiedComparator());

            LogUtil.d(TAG, "需要清理" + removeNum, "个缓存文件");
            for (int i = 0; i < removeNum; i++) {

                if (files[i].getName().contains(CACHE_FILE_SUFFIX)) {
                    files[i].delete();
                }

            }
        } else {
            LogUtil.d(TAG, "缓存情况没有超出指定范围， 无需清理。");
        }

        return true;
    }

    /**
     * 根据文件的最后修改时间进行排序
     */
    private class FileLastModifiedComparator implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * 修改文件的最后修改时间
     * 
     * @param path 文件绝对路径
     */
    public void updateFileTime(String path) {
        File file = new File(path);
        file.setLastModified(System.currentTimeMillis());
    }

    /**
     * 根据图片url生成文件缓存的名字
     * 
     * @param url 图片url
     * @return 缓存文件名
     */
    private String convertUrlToFileName(String url) {

        String fileName = MD5.getMD5Str(url) + CACHE_FILE_SUFFIX;

        LogUtil.d(TAG, "convertUrlToFileName(", url, ") = ", fileName);

        return fileName;
    }
	
}

