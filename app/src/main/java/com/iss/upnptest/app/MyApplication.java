package com.iss.upnptest.app;

import android.app.Application;

import com.iss.upnptest.moduls.searchdevice.entity.DeviceDisplay;
import com.iss.upnptest.upnp.UpnpServiceBiz;

import org.fourthline.cling.android.FixedAndroidLogHandler;
import org.fourthline.cling.support.model.item.Item;
import org.seamless.util.logging.LoggingUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-4-17
 */

public class MyApplication extends Application {

	private DeviceDisplay device;
	private Item item;
	private static MyApplication mApp;
	
	private static final Object LOCK = new Object();

	@Override
	public void onCreate() {
		super.onCreate();
		// Fix the logging integration between java.util.logging and Android
		// internal logging
		LoggingUtil.resetRootHandler(new FixedAndroidLogHandler());
		// Now you can enable logging as needed for various categories of Cling:
		// Logger.getLogger("org.fourthline.cling").setLevel(Level.FINEST);
		mApp = this;
	}

	public static MyApplication getInstance() {
		synchronized (LOCK) {
			return mApp;
		}
	}
	
	public void setDeviceDisplay(DeviceDisplay device) {
		this.device = device;
	}
	
	public DeviceDisplay getDeviceDisplay() {
		return device;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}
	
	@Override
	public void onTerminate() {
		UpnpServiceBiz.newInstance().closeUpnpService(this);
		super.onTerminate();
	}
	
}
