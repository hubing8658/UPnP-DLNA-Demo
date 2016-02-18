package com.iss.upnptest.moduls.avtransport.ui;

import java.util.HashMap;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.support.model.item.Item;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.iss.upnptest.R;
import com.iss.upnptest.app.MyApplication;
import com.iss.upnptest.moduls.avtransport.bll.RealtimeUpdatePositionInfo;
import com.iss.upnptest.moduls.avtransport.bll.MediaControlBiz;
import com.iss.upnptest.moduls.avtransport.bll.MediaEventBiz;
import com.iss.upnptest.moduls.avtransport.constants.MediaControlWhat;
import com.iss.upnptest.moduls.avtransport.constants.MediaEventWhat;
import com.iss.upnptest.moduls.avtransport.constants.TransportState;
import com.iss.upnptest.moduls.avtransport.entity.AVTransportInfo;
import com.iss.upnptest.moduls.avtransport.entity.RenderingControlInfo;

/**
 * @author hubing
 * @version 1.0.0 2015-4-23
 */

public class MediaControlActivity extends Activity implements OnClickListener {

	private ImageButton ibStop;
	protected ImageButton ibPlay;
	protected ImageButton ibPause;
	private ImageButton ibPlaypre;
	private ImageButton ibPlaynext;
	private SeekBar sbPlayback;
	protected TextView tvTotalTime;
	protected TextView tvCurrVol;
	private Button btnAddVol;
	private Button btnSubVol;
	private Item item;
	private ImageView ivMute;
	
	private Handler handler;
	private RealtimeUpdatePositionInfo realtimeUpdate;
	protected MediaControlBiz controlBiz;
	
	private long mId; // item播放实例id
	protected int currVolume;
	private boolean currentMute;
	private MediaEventBiz eventBiz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_control);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MediaControlWhat.SET_AVTRANSPORT_URI:
					controlBiz.getVolume();
					break;
				case MediaControlWhat.PLAY:
					updatePlayingState(true);
					break;
				case MediaControlWhat.PAUSE:
					updatePlayingState(false);
					break;
				case MediaControlWhat.STOP:
					updatePlayingState(false);
					break;
				case MediaControlWhat.GET_VOLUME:
					currVolume = msg.arg2;
					tvCurrVol.setText(getString(R.string.current_volume)
							+ currVolume);
					break;
				case MediaControlWhat.SET_VOLUME:
					tvCurrVol.setText(getString(R.string.current_volume)
							+ currVolume);
					break;
				case MediaControlWhat.SET_MUTE:
					ivMute.setSelected(currentMute);
					break;
				case MediaEventWhat.RENDERING_CONTROL:
					RenderingControlInfo rcInfo = (RenderingControlInfo) msg.obj;
					HashMap<String, Boolean> vChange = rcInfo.getValueIsChange();
					if (vChange.get(RenderingControlInfo.MUTE)) {
						currentMute = rcInfo.isMute();
						ivMute.setSelected(currentMute);
					}
					if (vChange.get(RenderingControlInfo.VOLUME)) {
						currVolume = rcInfo.getVolume();
						tvCurrVol.setText(getString(R.string.current_volume)
								+ currVolume);
					}
					break;
				case MediaEventWhat.AV_TRANSPORT:
					AVTransportInfo avtInfo = (AVTransportInfo) msg.obj;
					HashMap<String, Boolean> currStates = avtInfo.getValueIsChange();
					if (currStates.get(AVTransportInfo.CURRENT_MEDIA_DURATION)) {
						tvTotalTime.setText(avtInfo.getCurrentMediaDuration());
					}
					if (currStates.get(AVTransportInfo.TRANSPORT_STATE)) {
						String currState = avtInfo.getTransportState();
						if (TransportState.PLAYING.equals(currState)) {
							updatePlayingState(true);
						} else {
							updatePlayingState(false);
						}
					}
					break;
				}

			}
		};
		init();
		initView();
		setListener();
		// 设置dmr播放的uri
		controlBiz.setPlayUri(item);
	}

	protected void updatePlayingState(boolean isPlaying) {
		realtimeUpdate.setPlaying(isPlaying);
		if (isPlaying) {
			ibPlay.setVisibility(View.GONE);
			ibPause.setVisibility(View.VISIBLE);
		} else {
			ibPlay.setVisibility(View.VISIBLE);
			ibPause.setVisibility(View.GONE);
		}
	}
	
	private void init() {
		MyApplication app = (MyApplication) getApplication();
		Device device = app.getDeviceDisplay().getDevice();
		item = app.getItem();
		mId = 0;
		currentMute = false;
		controlBiz = new MediaControlBiz(device, handler, mId);
		eventBiz = new MediaEventBiz(device, handler);
	}

	private void initView() {
		// 停止
		ibStop = (ImageButton) findViewById(R.id.ib_stop);
		// 播放
		ibPlay = (ImageButton) findViewById(R.id.ib_play);
		// 暂停
		ibPause = (ImageButton) findViewById(R.id.ib_pause);
		// 上一首
		ibPlaypre = (ImageButton) findViewById(R.id.ib_playpre);
		// 下一首
		ibPlaynext = (ImageButton) findViewById(R.id.ib_playnext);
		// 静音
		ivMute = (ImageView) findViewById(R.id.iv_mute);
		// 当前音量大小
		tvCurrVol = (TextView) findViewById(R.id.tv_curr_vol);
		// 增加音量
		btnAddVol = (Button) findViewById(R.id.btn_add_vol);
		// 降低音量
		btnSubVol = (Button) findViewById(R.id.btn_sub_vol);

		// 播放音乐的进度
		sbPlayback = (SeekBar) findViewById(R.id.sb_playback);
		// 当前播放的时间
		TextView tvCurTime = (TextView) findViewById(R.id.tv_curTime);
		// 总时间
		tvTotalTime = (TextView) findViewById(R.id.tv_totalTime);

		// 启动从DMP设备更新播放状态
		realtimeUpdate = new RealtimeUpdatePositionInfo(controlBiz, sbPlayback,
				tvCurTime, tvTotalTime);
		realtimeUpdate.execute();
		eventBiz.addRenderingEvent();
		eventBiz.addAVTransportEvent();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 页面关闭时，关闭更新
		realtimeUpdate.setPlaying(false);
		realtimeUpdate.cancel(true);
		eventBiz.removeRenderingEvent();
		eventBiz.removeAVTransportEvent();
	}

	private void setListener() {
		ibStop.setOnClickListener(this);
		ibPlay.setOnClickListener(this);
		ibPause.setOnClickListener(this);
		ibPlaypre.setOnClickListener(this);
		ibPlaynext.setOnClickListener(this);
		ivMute.setOnClickListener(this);
		btnAddVol.setOnClickListener(this);
		btnSubVol.setOnClickListener(this);
		sbPlayback.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int prog = seekBar.getProgress();
				String totalTime = tvTotalTime.getText().toString();
				controlBiz.seek(totalTime, prog);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_stop:
			controlBiz.stop();
			break;
		case R.id.ib_play:
			controlBiz.play();
			break;
		case R.id.ib_pause:
			controlBiz.pause();
			break;
		case R.id.ib_playpre:
			controlBiz.previous();
			break;
		case R.id.ib_playnext:
			controlBiz.next();
			break;
		case R.id.iv_mute:
			if (currentMute) {
				currentMute = false;
			} else {
				currentMute = true;
			}
			controlBiz.setMute(currentMute);
			break;
		case R.id.btn_add_vol:
			currVolume++;
			if (currVolume > 100) {
				currVolume = 100;
				Toast.makeText(this, "已经是最大音量了", Toast.LENGTH_SHORT).show();
				return;
			}
			controlBiz.setVolume(currVolume);
			break;
		case R.id.btn_sub_vol:
			currVolume--;
			if (currVolume < 0) {
				currVolume = 0;
				Toast.makeText(this, "已经是最小音量了", Toast.LENGTH_SHORT).show();
				return;
			}
			controlBiz.setVolume(currVolume);
			break;
		}
	}

}
