package com.iss.upnptest.component.filedownload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import android.net.http.AndroidHttpClient;
import android.os.Environment;
import com.iss.upnptest.utils.FiletypeUtil;
import com.iss.upnptest.utils.LogUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-5-19
 */

public class HttpDownload {

private static final String TAG = HttpDownload.class.getSimpleName();
	
	public interface DownloadListener {
		void fileExists();
		void started();
		void finished();
		void onProgress(int progress);
		void failed();
	}
	
	private static String localPath;
	private static String imagePath;
	private static String audioPath;
	private static String videoPath;
	private static String otherPath;
	
	private DownloadListener listener;
	
	public HttpDownload() {
		localPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/iss/upnp/";
		imagePath = localPath + "image/";
		audioPath = localPath + "audio/";
		videoPath = localPath + "video/";
		otherPath = localPath + "other/";
	}
	
	/**
	 * 设置下载监听
	 * @param listener
	 */
	public void setDownloadListener(DownloadListener listener) {
		this.listener = listener;
	}
	
	/**
	 * 下载文件
	 * @param url
	 * @param localpath
	 * @param listener
	 */
	public void download(String url, String filename) {
		HttpClient client = AndroidHttpClient.newInstance("android mobile");
		HttpGet get = new HttpGet(url);
		File file = null;
		
		try {
			HttpResponse response = client.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				LogUtil.w(TAG, "Http Error：" + statusCode, " from ", url);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 下载的文件保存位置
				String path = otherPath;
				switch (FiletypeUtil.getFiletype(entity.getContentType().getValue())) {
				case FiletypeUtil.FILETYPE_MOVIE:
					path = videoPath;
					break;
				case FiletypeUtil.FILETYPE_MUSIC:
					path = audioPath;
					break;
				case FiletypeUtil.FILETYPE_PIC:
					path = imagePath;
					break;
				}
				String filepath = filename + "." + getNetworkFiletype(entity);
				
				File parentFile = new File(path);
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				
				file = new File(parentFile, filepath);
				
				// 下载的图片大小
				long size = entity.getContentLength();
				
				// 判断文件是否已下载
				if (file.exists() && file.length() == size) {
					if (listener != null) {
						listener.fileExists();
					}
					return;
				}
				
				// 按百分比的文件大小(更新进度时使用)
				long baseProgSize = size / 100;
				
				InputStream is = null;
				OutputStream os = null;
				try {
					is = entity.getContent();
					os = new FileOutputStream(file);
					
					byte[] buffer = new byte[8192];
					int n = 0;
					// 下载进度
					int prog = 0;
					// 当前已下载文件大小
					long currSize = 0;
					long mBaseProgSize = baseProgSize;
					
					// 开始下载
					if (listener != null) {
						listener.started();
					}
					
					while ((n = is.read(buffer)) != -1) {
						os.write(buffer, 0, n);
						currSize += n;
						
						// 更新下载进度
						if (listener != null && currSize >= mBaseProgSize) {
							prog++;
							listener.onProgress(prog);
							mBaseProgSize = mBaseProgSize + baseProgSize;
						}
					}
					os.flush();
					
					// 下载完成
					if (listener != null) {
						listener.finished();
					}
				} finally {
					if (is != null) {
						is.close();
					}
					if (os != null) {
						os.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			get.abort();
			
			// 下载失败
			if (listener != null) {
				listener.failed();
			}
			if (file != null && file.exists()) {
				file.delete();
			}
			LogUtil.e(TAG, e, "I/O error from ", url);
		} catch (Exception e) {
			get.abort();
			
			// 下载失败
			if (listener != null) {
				listener.failed();
			}
			if (file != null && file.exists()) {
				file.delete();
			}
			LogUtil.e(TAG, e, "Error from ", url);
		} finally {
			if (client instanceof AndroidHttpClient) {
				((AndroidHttpClient) client).close();
			}
		}
	}
	
	/**
	 * 获取网络文件类型(后缀)
	 * @param entity
	 * @return
	 */
	public String getNetworkFiletype(HttpEntity entity) {
		String filetype = "unkown";
		Header h = entity.getContentType();
		if (h != null) {
			String v = h.getValue();
			if (v != null) {
				filetype = v.split("/")[1];
			}
		}
		return filetype;
	}
	
}

