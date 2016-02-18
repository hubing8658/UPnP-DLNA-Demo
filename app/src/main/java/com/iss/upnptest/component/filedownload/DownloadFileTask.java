package com.iss.upnptest.component.filedownload;

import java.util.concurrent.Callable;
import android.os.Handler;
import android.os.Message;
import com.iss.upnptest.component.filedownload.HttpDownload.DownloadListener;

/**
 * 下载文件子线程任务
 * 
 * @author hubing
 * @version 1.0.0 2015-5-20
 */

public class DownloadFileTask implements Callable<String> {

	private String url;
	private String filename;
	private Handler handler;

	public DownloadFileTask(Handler handler, String url, String filename) {
		this.handler = handler;
		this.url = url;
		this.filename = filename;
	}

	@Override
	public String call() throws Exception {
		startDownload();
		return url;
	}

	/**
	 * 开始下载文件
	 */
	private void startDownload() {
		HttpDownload httpDownload = new HttpDownload();
		// 设置下载监听
		httpDownload.setDownloadListener(new DownloadListener() {
			@Override
			public void onProgress(int progress) {
				sendMsg(DownloadStatus.UPDATE_PROGRESS, progress);
			}

			@Override
			public void fileExists() {
				sendMsg(DownloadStatus.FILE_EXSITS, -1);
			}

			@Override
			public void started() {
				sendMsg(DownloadStatus.STARTED, -1);
			}

			@Override
			public void finished() {
				sendMsg(DownloadStatus.FINISHED, -1);
			}

			@Override
			public void failed() {
				sendMsg(DownloadStatus.FAILED, -1);
			}
		});
		// 开始下载
		httpDownload.download(url, filename);
	}

	/**
	 * 发送消息
	 * @param what
	 * @param prog
	 */
	protected void sendMsg(int what, int prog) {
		Message msg = Message.obtain(handler);
		msg.what = what;
		msg.arg1 = prog;
		msg.sendToTarget();
	}

}
