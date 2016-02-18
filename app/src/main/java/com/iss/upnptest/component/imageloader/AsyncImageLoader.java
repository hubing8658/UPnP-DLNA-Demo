package com.iss.upnptest.component.imageloader;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.iss.upnptest.component.imageloader.ImageHttpDownload.DownloadProgressListener;
import com.iss.upnptest.component.imagview.ProgressImageView;
import com.iss.upnptest.utils.LogUtil;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;

/**
 * @version 1.0.0 2015-5-14
 */

public class AsyncImageLoader {

	private static final String TAG = AsyncImageLoader.class.getSimpleName();
	
	/** 线程池线程数量 */
	private static final int THREAD_NUM = 5;
	/** 线程池 */
	private ExecutorService executorService; 
	/** 内存缓存对象 */
	private BitmapMemoryCache memCache;
	/** 文件缓存对象 */
	private ImageFileCache fileCache;
	/** 需要加载的图片集合 */
	private SparseArray<ProgressImageView> todoImgs;
	
	private static AsyncImageLoader instance;

	// 单例模式
	private AsyncImageLoader() {
		executorService = Executors.newFixedThreadPool(THREAD_NUM);

		memCache = new BitmapMemoryCache();
		fileCache = new ImageFileCache();
		todoImgs = new SparseArray<ProgressImageView>();
	}
	
	/**
	 * 
	 * 获取实例
	 * @return
	 */
	public static synchronized AsyncImageLoader getInstance(){
		if(instance == null){
			instance = new AsyncImageLoader();
		}
		return instance;
	}

	/**
	 * 
	 * 请求显示图片，在有网络图片需要展示时调用此方法；<br>
	 * 调用此方法后，不会立即从网络或者文件中获取图片数据；<br>
	 * 只会先展示内存缓存中存在的图片数据，<br>
	 * 而内存缓存中不存在的图片数据则是先放入待办任务列表中，等调用loadImg方法时才会真正的进行加载数据。<br>
	 * 
	 * @param url 网络图片的网址
	 * @param img 用于显示图片的ImageView对象
	 */
	public void showImg(String url, ProgressImageView img) {
		
		// 先从内存缓存中获取，如果获取到了，直接展示
		Bitmap imgData = memCache.getBitmap(url);
		if (imgData != null) {
			LogUtil.d(TAG, "showImg，从内存缓存中获取到图片数据，url = ", url);
			img.setImageBitmap(imgData);
		} else {
			LogUtil.d(TAG, "showImg，从内存缓存中未获取到图片数据，将图片加入待办任务，url = ", url);
			// 如果从内存缓存中获取不到，加入待办任务
			synchronized (todoImgs) {
				// 使用ImageView对象的hashCode()值做为键，可以解决ListView中getView因对象重复使用而导致的重复问题
			    img.setTag(url);
			    todoImgs.put(img.hashCode(), img);
			}
		}
	}

	/**
	 * 触发加载图片任务。调用showImg方法后，必须调用此方法才会真正的开始加载图片数据。<br>
	 * 建议此方法在界面滚动停止后调用。<br>
	 * eg: 1、在OnScrollListener的onScrollStateChanged方法中调用【 if(OnScrollListener.SCROLL_STATE_IDLE == scrollState)】<br>
	 * 2、在Adapter的getView方法中调用，当convertView为null，新创建Vew对象时调用。<br>
	 */
	public void loadImg() {
		
	    synchronized (todoImgs) {
            int size = todoImgs.size();
            if(size > 0){
                LogUtil.d(TAG, "loadImg，待办任务中有" + size, "条新的图片数据需要获取");
                ProgressImageView iv ;
                for(int i=0; i< size; i++){
                    iv = todoImgs.valueAt(i);
                    if(iv != null && iv.getTag() != null){
                        loadImageData((String) iv.getTag(), iv);
                    }
                }
                todoImgs.clear();
            }else{
                LogUtil.d(TAG, "loadImg，待办任务中没有新的图片数据");
            }
        }
	}
	
	/**
	 * 
	 * 立即加载图片，做为showImg方法的补充。主要用于非ListView中图片的加载使用。
	 * 
	 * @param url 网络图片的网址
	 * @param img 用于显示图片的ImageView对象
	 */
	public void showImgImmediately(String url, ProgressImageView img) {
		// 先从内存缓存中获取，如果获取到了，直接展示
		Bitmap imgData = memCache.getBitmap(url);
		if (imgData != null) {
			LogUtil.d(TAG, "showImgImmediately，从内存缓存中获取到图片数据，url = ", url);
			img.setImageBitmap(imgData);
		} else {
			// 如果从内存缓存中获取不到，立即触发从网络或文件中加载数据
			LogUtil.d(TAG, "showImgImmediately，从内存缓存中未获取到图片数据，立即触发从网络或文件中加载数据，url = ", url);
			img.setTag(url);
			loadImageData(url, img);
		}
	}

	/**
	 * 
	 * 加载图片数据
	 * @param url 网络图片的网址
	 * @param img 用于显示图片的ImageView对象
	 */
	private void loadImageData(String url, ProgressImageView img) {
		executorService.submit(new ImgLoadTask(new ImgHandler(url, img), url));
	}

	/**
	 * 
	 * 获得一个图片的数据,从三个地方获取,首先是内存缓存,然后是文件缓存,最后从网络获取
	 * @param url 图片网络url
	 * @param listener 进度监听
	 * @return 图片数据的Bitmap对象
	 */
	private Bitmap getImgData(String url, DownloadProgressListener listener) {
		// 从内存缓存中获取图片
		Bitmap imgData = memCache.getBitmap(url);
		if (imgData == null) {
			// 从文件缓存中获取
			imgData = fileCache.getImgData(url);
			if (imgData == null) {
				ImageHttpDownload fileDownload = new ImageHttpDownload();
				// 从网络获取
				imgData = fileDownload.getImage(url, listener);
				if (imgData != null) {
					LogUtil.d(TAG, "从网络获取到了图片数据，url = ", url);
					memCache.putBitmap(url, imgData);
					fileCache.saveBitmapToSdcard(imgData, url);
				}else{
					LogUtil.d(TAG, "获取图片数据失败，url = ", url);
				}
			} else {
				LogUtil.d(TAG, "从文件缓存获取到了图片数据，url = ", url);
				// 添加到内存缓存
				memCache.putBitmap(url, imgData);
			}
		}else{
			LogUtil.d(TAG, "从内存缓存获取到了图片数据，url = ", url);
		}
		return imgData;
	}

	/**
	 * Handler，用于在图片异步加载完成后，显示图片到界面
	 */
	protected static class ImgHandler extends Handler {
		
		public static final int WHAT_FINISHED = 0;
		public static final int WHAT_PROG = 1;
		
		private String url;
		private ProgressImageView img;

		public ImgHandler(String url, ProgressImageView img) {
			this.url = url;
			this.img = img;
		}

		@Override
		public void handleMessage(Message msg) {
			// 显示图片前，需要确认Imageview请求的url是否与现在需要显示的图片url一致
			// （ListView中的图片可能会出现改变的情况）
			if (img.getTag().equals(url)) {
				switch (msg.what) {
				case WHAT_FINISHED:
					Bitmap imgData = (Bitmap) msg.obj;
					if(imgData != null){
						img.setImageBitmap(imgData);
					}
					break;
				case WHAT_PROG:
					int prog = msg.arg1;
					img.setLoadProgress(prog);
					break;
				}
			}
		}
	}

	/**
	 *  图片加载的子线程任务 
	 */
	private class ImgLoadTask implements Callable<String> {
		private String url;
		private Handler handler;

		public ImgLoadTask(Handler handler, String url) {
			this.url = url;
			this.handler = handler;
		}

		@Override
		public String call() {
			Bitmap bm = getImgData(url, new DownloadProgressListener() {
				@Override
				public void onProgress(int progress) {
					Message msg = Message.obtain();
					msg.what = ImgHandler.WHAT_PROG;
					msg.arg1 = progress;
					handler.sendMessage(msg);
				}
			});
			Message msg = Message.obtain();
			if (bm != null) {
				msg.what = ImgHandler.WHAT_FINISHED;
				msg.obj = bm;
				handler.sendMessage(msg);
			}
			return url;
		}

	}
	
}

