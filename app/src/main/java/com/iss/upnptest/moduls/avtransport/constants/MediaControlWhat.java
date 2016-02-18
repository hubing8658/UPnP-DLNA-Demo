package com.iss.upnptest.moduls.avtransport.constants;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public class MediaControlWhat {

	// 获取当前静音状态
	public static final int GET_MUTE = 10;
	// 打开/关闭静音模式
	public static final int SET_MUTE = 11;
	// 获取当前音量大小
	public static final int GET_VOLUME = 12;
	// 设置音量大小
	public static final int SET_VOLUME = 13;
	
	// 停止
	public static final int STOP = 14;
	// 播放
	public static final int PLAY = 15;
	// 暂停
	public static final int PAUSE = 16;
	// 跳转到指定位置
	public static final int SEEK = 17;
	// 下一首
	public static final int NEXT = 18;
	// 上一首
	public static final int PREVIOUS = 19;
	// 设置播放URI
	public static final int SET_AVTRANSPORT_URI = 20;
	// 获取媒体信息
	public static final int GET_MEDIAINFO = 21;
	// 获取播放位置信息
	public static final int GET_POSITIONINFO = 22;
	// 获取传输信息
	public static final int GET_TRANSPORT_INFO = 23;
	// 获取当前传输action
	public static final int GET_CURRENT_TRANSPORT_ACTION = 24;
	// 获取设备支持的功能
	public static final int GET_DEVICE_CAPABILITIES = 25;

}
