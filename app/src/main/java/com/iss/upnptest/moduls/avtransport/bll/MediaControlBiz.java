package com.iss.upnptest.moduls.avtransport.bll;

import org.fourthline.cling.model.ModelUtil;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.item.Item;
import android.os.Handler;
import android.os.Message;
import com.iss.upnptest.moduls.avtransport.callback.avtransport.GetMediaInfo;
import com.iss.upnptest.moduls.avtransport.callback.avtransport.GetPositionInfo;
import com.iss.upnptest.moduls.avtransport.callback.avtransport.Next;
import com.iss.upnptest.moduls.avtransport.callback.avtransport.Pause;
import com.iss.upnptest.moduls.avtransport.callback.avtransport.Play;
import com.iss.upnptest.moduls.avtransport.callback.avtransport.Previous;
import com.iss.upnptest.moduls.avtransport.callback.avtransport.Seek;
import com.iss.upnptest.moduls.avtransport.callback.avtransport.SetAVTransportURI;
import com.iss.upnptest.moduls.avtransport.callback.avtransport.Stop;
import com.iss.upnptest.moduls.avtransport.callback.renderingcontrol.GetMute;
import com.iss.upnptest.moduls.avtransport.callback.renderingcontrol.GetVolume;
import com.iss.upnptest.moduls.avtransport.callback.renderingcontrol.SetMute;
import com.iss.upnptest.moduls.avtransport.callback.renderingcontrol.SetVolume;
import com.iss.upnptest.moduls.avtransport.constants.MediaControlWhat;
import com.iss.upnptest.moduls.avtransport.entity.MediaInfo;
import com.iss.upnptest.moduls.avtransport.entity.PositionInfo;
import com.iss.upnptest.upnp.UpnpServiceBiz;
import com.iss.upnptest.utils.LogUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public class MediaControlBiz {

	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;

	private static String TAG = MediaControlBiz.class.getSimpleName();

	private Service serviceAVT;
	private Service serviceRC;
	private UpnpServiceBiz upnpBiz;
	protected Handler handler;
	private UnsignedIntegerFourBytes instanceId;

	public MediaControlBiz(Device device, Handler handler, long mId) {
		serviceAVT = device.findService(new UDAServiceType("AVTransport", 1));
		serviceRC = device
				.findService(new UDAServiceType("RenderingControl", 1));
		upnpBiz = UpnpServiceBiz.newInstance();
		this.handler = handler;
		instanceId = new UnsignedIntegerFourBytes(mId);
	}

	/**
	 * 设备要播放的媒体的uri
	 * @param item
	 */
	public void setPlayUri(Item item) {
		String uri = item.getFirstResource().getValue();
		DIDLContent didlContent = new DIDLContent();
		didlContent.addItem(item);
		DIDLParser parser = new DIDLParser();
		String metadata = "";
		try {
			metadata = parser.generate(didlContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		upnpBiz.execute(new SetAVTransportURI(instanceId, serviceAVT, uri,
				metadata) {

			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "setPlayUri failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(String defaultMsg) {
				LogUtil.d(TAG, "SetAVTransportURI successed:" + defaultMsg);
				Message msg = Message.obtain(handler);
				msg.what = MediaControlWhat.SET_AVTRANSPORT_URI;
				msg.arg1 = SUCCESS;
				msg.sendToTarget();
			}
		});
	}

	public interface GetPositionInfoListerner {
		void failure(ActionInvocation invocation, UpnpResponse operation,
				String defaultMsg);
		void onSuccess(PositionInfo positionInfo);
	}
	
	protected GetPositionInfoListerner gpiListener;
	
	/**
	 * 获取当前播放的媒体位置信息
	 * @author hubing
	 */
	public void getPositionInfo(GetPositionInfoListerner listener) {
		this.gpiListener = listener;
		upnpBiz.execute(new GetPositionInfo(instanceId, serviceAVT) {
			
			@Override
			public void failure(ActionInvocation invocation, UpnpResponse operation,
					String defaultMsg) {
				LogUtil.d(TAG, "Get position info failure:" + defaultMsg);
				if (gpiListener != null) {
					gpiListener.failure(invocation, operation, defaultMsg);
				}
			}
			
			@Override
			public void onSuccess(PositionInfo positionInfo) {
				if (gpiListener != null) {
					gpiListener.onSuccess(positionInfo);
				}
			}
		});
	}
	
	/**
	 * 获取当前播放的媒体信息
	 */
	public void getMediaInfo() {
		upnpBiz.execute(new GetMediaInfo(instanceId, serviceAVT) {

			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "GetMediaInfo failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(MediaInfo mediaInfo) {

			}
		});
	}

	/**
	 * 播放
	 * @author hubing
	 */
	public void play() {
		upnpBiz.execute(new Play(instanceId, serviceAVT) {
			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "Play failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(String defaultMsg) {
				LogUtil.d(TAG, "Play successed:" + defaultMsg);
				Message msg = Message.obtain(handler);
				msg.what = MediaControlWhat.PLAY;
				msg.arg1 = SUCCESS;
				msg.sendToTarget();
			}
		});
	}

	/**
	 * 暂停
	 */
	public void pause() {
		upnpBiz.execute(new Pause(instanceId, serviceAVT) {
			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "Pause failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(String defaultMsg) {
				LogUtil.d(TAG, "Pause successed:" + defaultMsg);
				Message msg = Message.obtain(handler);
				msg.what = MediaControlWhat.PAUSE;
				msg.arg1 = SUCCESS;
				msg.sendToTarget();
			}
		});
	}

	/**
	 * 下一首
	 */
	public void next() {
		upnpBiz.execute(new Next(instanceId, serviceAVT) {
			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "Next failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(String defaultMsg) {
				LogUtil.d(TAG, "Next successed:" + defaultMsg);
				Message msg = Message.obtain(handler);
				msg.what = MediaControlWhat.NEXT;
				msg.arg1 = SUCCESS;
				msg.sendToTarget();
			}
		});
	}

	/**
	 * 上一首
	 */
	public void previous() {
		upnpBiz.execute(new Previous(instanceId, serviceAVT) {
			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "Previous failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(String defaultMsg) {
				LogUtil.d(TAG, "Previous successed:" + defaultMsg);
				Message msg = Message.obtain(handler);
				msg.what = MediaControlWhat.PREVIOUS;
				msg.arg1 = SUCCESS;
				msg.sendToTarget();
			}
		});
	}

	/**
	 * 停止播放
	 */
	public void stop() {
		upnpBiz.execute(new Stop(instanceId, serviceAVT) {
			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "Stop failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(String defaultMsg) {
				LogUtil.d(TAG, "Stop successed:" + defaultMsg);
				Message msg = Message.obtain(handler);
				msg.what = MediaControlWhat.STOP;
				msg.arg1 = SUCCESS;
				msg.sendToTarget();
			}
		});
	}

	/**
	 * 设置跳转时间
	 * @param totalTime 媒体总时长
	 * @param percent 跳转的百分比
	 */
	public void seek(String totalTime, int percent) {
		long duration = ModelUtil.fromTimeString(totalTime);
		long seekTime = percent * duration / 100;
		String seekTo = ModelUtil.toTimeString(seekTime);
		upnpBiz.execute(new Seek(instanceId, serviceAVT, seekTo) {
			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "Seek failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(String defaultMsg) {
				LogUtil.d(TAG, "Seek successed:" + defaultMsg);
				Message msg = Message.obtain(handler);
				msg.what = MediaControlWhat.SEEK;
				msg.arg1 = SUCCESS;
				msg.sendToTarget();
			}
		});
	}

	/**
	 * 获取当前静音状态
	 */
	public void getMute() {
		upnpBiz.execute(new GetMute(serviceRC, instanceId) {

			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "GetVolume failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(boolean currentMute) {
				LogUtil.d(TAG, "GetVolume successed:current mute = "
						+ currentMute);
				Message msg = Message.obtain(handler);
				msg.what = MediaControlWhat.GET_MUTE;
				msg.arg1 = SUCCESS;
				msg.obj = currentMute;
				msg.sendToTarget();
			}
		});
	}

	/**
	 * 设置是否开启静音模式
	 * @param desiredMute true表示开启静音
	 */
	public void setMute(boolean desiredMute) {
		upnpBiz.execute(new SetMute(serviceRC, instanceId, desiredMute) {

			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "GetVolume failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(String defaultMsg) {
				LogUtil.d(TAG, "SetMute successed:" + defaultMsg);
				Message msg = Message.obtain(handler);
				msg.what = MediaControlWhat.SET_MUTE;
				msg.arg1 = SUCCESS;
				msg.sendToTarget();
			}
		});
	}

	/**
	 * 获取当前音量大小
	 */
	public void getVolume() {
		upnpBiz.execute(new GetVolume(serviceRC, instanceId) {

			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "GetVolume failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(int currentVolume) {
				LogUtil.d(TAG, "GetVolume successed:current volume = "
						+ currentVolume);
				Message msg = Message.obtain(handler);
				msg.what = MediaControlWhat.GET_VOLUME;
				msg.arg1 = SUCCESS;
				msg.arg2 = currentVolume;
				msg.sendToTarget();
			}
		});
	}

	/**
	 * 设置音量大小，范围0-100
	 * @param volume
	 */
	public void setVolume(int volume) {
		upnpBiz.execute(new SetVolume(serviceRC, instanceId, volume) {

			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "SetVolume failure:" + defaultMsg);
			}

			@Override
			public void onSuccess(String defaultMsg) {
				LogUtil.d(TAG, "SetVolume successed:" + defaultMsg);
				Message msg = Message.obtain(handler);
				msg.what = MediaControlWhat.SET_VOLUME;
				msg.arg1 = SUCCESS;
				msg.sendToTarget();
			}
		});
	}

}
