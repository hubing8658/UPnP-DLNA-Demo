package com.iss.upnptest.component.imageloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import com.iss.upnptest.utils.BitmapUtil;
import com.iss.upnptest.utils.LogUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;

/**
 * @author hubing
 * @version 1.0.0 2015-5-14
 */

public class ImageHttpDownload {

	private static final String TAG = ImageHttpDownload.class.getSimpleName();
	
	private DownloadProgressListener dpListener;
	
	public interface DownloadProgressListener {
		void onProgress(int progress);
	}
	
	public Bitmap getImage(String url, DownloadProgressListener listener) {
		this.dpListener = listener;
		Bitmap bm = null;
		HttpClient client = AndroidHttpClient.newInstance("android mobile");
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				LogUtil.w(TAG, "Http Error：" + statusCode, " from ", url);
				return bm;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 下载的图片大小
				long size = entity.getContentLength();
				// 按百分比的文件大小(更新进度时使用)
				long baseProgSize = size / 100;
				InputStream is = null;
				try {
					is = entity.getContent();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[8192];
					int n = 0;
					// 下载进度
					int prog = 0;
					// 当前已下载文件大小
					long currSize = 0;
					long mBaseProgSize = baseProgSize;
					while ((n = is.read(buffer)) != -1) {
						baos.write(buffer, 0, n);
						currSize += n;
						if (dpListener != null && currSize >= mBaseProgSize) {
							prog++;
							dpListener.onProgress(prog);
							mBaseProgSize = mBaseProgSize + baseProgSize;
						}
					}
					if (dpListener != null) {
						prog = 100;
						dpListener.onProgress(prog);
					}
					byte[] bytes = baos.toByteArray();
					bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
					if (size > 2 * 1024 * 1024) {
						bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, BitmapUtil.calculateOptions(bm, 480, 800));
					}
					return bm;
				} finally {
					if (is != null) {
						is.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			get.abort();
			LogUtil.e(TAG, e, "I/O error from ", url);
		} catch (Exception e) {
			get.abort();
			LogUtil.e(TAG, e, "Error from ", url);
		} finally {
			if (client instanceof AndroidHttpClient) {
				((AndroidHttpClient) client).close();
			}
		}
		return bm;
	}
	
}

