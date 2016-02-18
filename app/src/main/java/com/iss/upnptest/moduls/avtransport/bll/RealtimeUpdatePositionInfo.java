package com.iss.upnptest.moduls.avtransport.bll;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import android.os.AsyncTask;
import android.widget.SeekBar;
import android.widget.TextView;
import com.iss.upnptest.moduls.avtransport.bll.MediaControlBiz.GetPositionInfoListerner;
import com.iss.upnptest.moduls.avtransport.entity.PositionInfo;
import com.iss.upnptest.utils.LogUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public class RealtimeUpdatePositionInfo extends AsyncTask<Void, PositionInfo, PositionInfo> {

	private static String TAG = RealtimeUpdatePositionInfo.class.getSimpleName();
	
	private boolean isPlaying; // 是否正在播放
	
	private SeekBar sbPlayback;
	private TextView tvCurTime;
	private TextView tvTotalTime;
	private MediaControlBiz controlBiz;
	
	public RealtimeUpdatePositionInfo(MediaControlBiz controlBiz, SeekBar sbPlayback, TextView tvCurTime, TextView tvTotalTime) {
		this.sbPlayback = sbPlayback;
		this.tvCurTime = tvCurTime;
		this.tvTotalTime = tvTotalTime;
		this.controlBiz = controlBiz;
		
		sbPlayback.setMax(100);
		isPlaying = true;
	}
	
	@Override
	protected PositionInfo doInBackground(Void... params) {
		PositionInfo info = null;
		while (!isCancelled()) {
			while (isPlaying) {
				try {
					Thread.sleep(1000);
					controlBiz.getPositionInfo(new GetPositionInfoListerner() {
						
						@Override
						public void onSuccess(PositionInfo positionInfo) {
							publishProgress(positionInfo);
						}
						
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation,
								String defaultMsg) {
							LogUtil.d(TAG, "Get position info failure:" + defaultMsg);
						}
					});
				} catch (InterruptedException e) {
					LogUtil.d(TAG, "Get position info failure:" + e.getMessage());
				}
			}
		}
		return info;
	}

	@Override
	protected void onProgressUpdate(PositionInfo... values) {
		PositionInfo info = values[0];
		sbPlayback.setProgress(info.getElapsedPercent());
		tvCurTime.setText(info.getRelTime());
		tvTotalTime.setText(info.getTrackDuration());
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * 设置是否暂停
	 * @param isPause
	 */
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		sbPlayback = null;
		tvCurTime = null;
		tvTotalTime = null;
		controlBiz = null;
	}
	
}

