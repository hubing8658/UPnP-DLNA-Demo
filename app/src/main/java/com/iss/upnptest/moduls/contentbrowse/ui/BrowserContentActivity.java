package com.iss.upnptest.moduls.contentbrowse.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.iss.upnptest.R;
import com.iss.upnptest.adapter.GeneralAdapter;
import com.iss.upnptest.app.MyApplication;
import com.iss.upnptest.moduls.avtransport.ui.MediaControlActivity;
import com.iss.upnptest.moduls.browseimage.ui.BroseImageActivity;
import com.iss.upnptest.moduls.contentbrowse.bll.ContentBrowseBiz;
import com.iss.upnptest.moduls.contentbrowse.entity.ContentItem;
import com.iss.upnptest.moduls.searchdevice.entity.DeviceDisplay;
import com.iss.upnptest.upnp.UpnpServiceBiz;
import com.iss.upnptest.upnp.constants.HandlerWhat;
import com.iss.upnptest.utils.FiletypeUtil;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.support.model.item.Item;

import java.util.ArrayList;

/**
 * @author hubing
 * @version 1.0.0 2015-4-16
 */

public class BrowserContentActivity extends AppCompatActivity {

	private GeneralAdapter<ContentItem> contentAdapter;
	private ListView lvContents;
	private DeviceDisplay dDevice;
	private ContentBrowseBiz browseBiz;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HandlerWhat.ADD:
					ContentItem item = (ContentItem) msg.obj;
					contentAdapter.add(item);
					break;
				case HandlerWhat.CLEAR_ALL:
					contentAdapter.clear();
					break;
				}
			}
		};
		init();
		initView();
		setListener();
	}

	private void init() {
		MyApplication app = (MyApplication) getApplication();
		dDevice = app.getDeviceDisplay();
		app.setDeviceDisplay(null);
		browseBiz = new ContentBrowseBiz(handler);
	}

	private void initView() {
		// 设置显示ToolBar
		Toolbar tbTitle = (Toolbar) findViewById(R.id.tb_title);
		setSupportActionBar(tbTitle);

		lvContents = (ListView) findViewById(R.id.lv_devices);
		contentAdapter = new ContentAdapter(this, R.layout.item_content, null);
		contentAdapter.setMaxCount(200);
		lvContents.setAdapter(contentAdapter);

		// 显示根容器数据
		browseBiz.getRootContent(dDevice.getDevice());
	}

	private void setListener() {
		lvContents.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ContentItem ct = contentAdapter.getItem(position);
				browserSubContainer(ct);
			}
		});
	}

	protected void browserSubContainer(ContentItem ct) {
		if (ct.isContainer()) {
			browseBiz.getContent(ct);
		} else {
			// 打开文件
			Item item = ct.getItem();
			Intent intent = null; 

			switch (ct.getFiletype()) {
			case FiletypeUtil.FILETYPE_PIC:
				intent = new Intent(this, BroseImageActivity.class);
				break;
			case FiletypeUtil.FILETYPE_MOVIE:
				break;
			case FiletypeUtil.FILETYPE_MUSIC:
				break;
			}
			if (intent != null) {
				MyApplication app = (MyApplication) getApplication();
				app.setItem(item);
				startActivity(intent);
			} else {
				showSelectDMP(item);
			}
		}
	}

	private void showSelectDMP(final Item item) {
		final Dialog dialog = new Dialog(this);
		ListView lvMDP = new ListView(this);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		dialog.addContentView(lvMDP, params);
		dialog.setTitle(R.string.select_render);
		dialog.show();
		final GeneralAdapter<Device> adapter = new GeneralAdapter<Device>(this, R.layout.item_content, null) {
			@Override
			public void convert(ViewHolder holder, Device item, int position) {
				DeviceDisplay dd = new DeviceDisplay(item);
				holder.setText(R.id.tv_title, dd.toString());
			}
		};
		lvMDP.setAdapter(adapter);
		ArrayList<Device> data = new ArrayList<Device>(
				UpnpServiceBiz.newInstance().getDevices(new UDAServiceType("AVTransport")));
		adapter.setData(data);
		lvMDP.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Device device = adapter.getItem(position);
				MyApplication app = (MyApplication) getApplication();
				app.setDeviceDisplay(new DeviceDisplay(device));
				app.setItem(item);
				dialog.dismiss();

				Intent intent = new Intent(BrowserContentActivity.this, MediaControlActivity.class);
				startActivity(intent);
			}
		});
	}

}
