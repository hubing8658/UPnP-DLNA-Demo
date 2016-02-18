package com.iss.upnptest.moduls.browseimage.ui;

import org.fourthline.cling.support.model.item.Item;
import com.iss.upnptest.R;
import com.iss.upnptest.app.MyApplication;
import com.iss.upnptest.component.filedownload.DownloadManager;
import com.iss.upnptest.component.imageloader.AsyncImageLoader;
import com.iss.upnptest.component.imagview.ProgressImageView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * @author hubing
 * @version 1.0.0 2015-5-14
 */

public class BroseImageActivity extends Activity {

	private String imageurl;
	private ProgressImageView iv;
	private String name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initViews();
	}

	private void init() {
		MyApplication app = (MyApplication) getApplication();
		Item item = app.getItem();
		app.setItem(null);
		imageurl = item.getFirstResource().getValue();
		name = item.getTitle();
	}

	private void initViews() {
		iv = new ProgressImageView(this);
		AsyncImageLoader imageLoader = AsyncImageLoader.getInstance();
		setContentView(iv);
		iv.setImageResource(R.drawable.ua_imgcachev2_img_default);
		imageLoader.showImgImmediately(imageurl, iv);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.select_show_device:
			
			break;
		case R.id.download:
			// 下载图片
			DownloadManager.newInstance(getApplication()).download(imageurl, name);
			break;
		}
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_browse_image, menu);
		return true;
	}
	
}

