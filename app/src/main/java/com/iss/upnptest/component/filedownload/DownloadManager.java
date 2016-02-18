package com.iss.upnptest.component.filedownload;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.iss.upnptest.R;
import com.iss.upnptest.utils.ToastUtil;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;

/**
 * @author hubing
 * @version 1.0.0 2015-5-20
 */

public class DownloadManager {

	// 线程池数量
	private static final int THREAD_NUM = 3;
	// 线程池
	private ExecutorService executorService;
	
	private static Application ctx;
	
	private static DownloadManager instance;
	private static ConcurrentHashMap<String, DownloadFileTask> tasks;
	
	private DownloadManager() {
		executorService = Executors.newFixedThreadPool(THREAD_NUM);
		tasks = new ConcurrentHashMap<String, DownloadFileTask>();
	}
	
	public static DownloadManager newInstance(Application context) {
		if (instance == null) {
			instance = new DownloadManager();
		}
		ctx = context;
		return instance;
	}
	
	/**
	 * 添加并开始下载
	 * @param url
	 * @param filename
	 */
	public void download(String url, String filename) {
		if (!tasks.containsKey(filename)) {
			DownloadFileTask task = new DownloadFileTask(new DownloadHandler(filename), url, filename);
			executorService.submit(task);
			tasks.put(filename, task);
		} else {
			// 已经在下载任务中
			ToastUtil.showShortToast(ctx, R.string.file_downloading);
		}
	}
	
	protected static class DownloadHandler extends Handler {
		
		private String filename;
		private NotificationManager notifiManager;
		private int nitifyId;
		private Builder builder;
		
		public DownloadHandler(String filename) {
			this.filename = filename;
			nitifyId = filename.hashCode();
			notifiManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DownloadStatus.FILE_EXSITS:
				ToastUtil.showShortToast(ctx, R.string.file_exists);
				// 文件已下载过，从下载任务列表中移除
				tasks.remove(filename);
				break;
			case DownloadStatus.FINISHED:
				String finishText = filename + ctx.getString(R.string.download_finished);
				ToastUtil.showShortToast(ctx, finishText);
				
				builder.setProgress(100, 100, false)
				.setTicker(finishText)
				.setContentText(finishText)
				.setContentInfo("100%")
				.setAutoCancel(true);
				
				notifiManager.notify(nitifyId, builder.build());
				// 下载完成，从下载任务列表中移除
				tasks.remove(filename);
				break;
			case DownloadStatus.FAILED:
				ToastUtil.showShortToast(ctx, filename + ctx.getString(R.string.download_failed));
				
				builder.setContentText(filename + ctx.getString(R.string.download_failed));
				builder.setAutoCancel(true);
				
				notifiManager.notify(nitifyId, builder.build());
				
				// 下载失败，从下载任务列表中移除
				tasks.remove(filename);
				break;
			case DownloadStatus.STARTED:
				ToastUtil.showShortToast(ctx, ctx.getString(R.string.download_started) + filename);
				
				// 显示到通知栏
				builder = new Builder(ctx)
				.setSmallIcon(R.drawable.download_icon)
				.setContentTitle(ctx.getString(R.string.download))
				.setTicker(ctx.getString(R.string.download_started) + filename)
				.setContentText(ctx.getString(R.string.download_ongoing) + filename)
				.setContentInfo("0%")
				.setProgress(100, 0, false)
				.setAutoCancel(false);
				
				notifiManager.notify(nitifyId, builder.build());
				break;
			case DownloadStatus.UPDATE_PROGRESS:
				int prog = msg.arg1;
				builder.setContentInfo(prog + "%")
				.setProgress(100, prog, false);
				notifiManager.notify(nitifyId, builder.build());
				break;
			}
		}
		
	}
	
}
