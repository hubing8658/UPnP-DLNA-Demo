package com.iss.upnptest.moduls.avtransport.entity;

import java.util.HashMap;

/**
 * @author hubing
 * @version 1.0.0 2015-5-5
 */

public class RenderingControlInfo {

	// 保存值的更改状态
	private HashMap<String, Boolean> valueIsChange = new HashMap<String, Boolean>();
	
	public static final String MUTE = "Mute";
	public static final String VOLUME = "Volume";
	public static final String PRESET_NAME_LIST = "PresetNameList";
	
	// 静音
	private boolean mute;
	// 音量大小
	private int volume;
	// 预设列表名
	private String presetNameList;
	
	public RenderingControlInfo() {
		valueIsChange.put(MUTE, false);
		valueIsChange.put(VOLUME, false);
		valueIsChange.put(PRESET_NAME_LIST, false);
	}
	
	public RenderingControlInfo(boolean mute, int volume, String presetNameList) {
		this.mute = mute;
		this.volume = volume;
		this.presetNameList = presetNameList;
		valueIsChange.put(MUTE, true);
		valueIsChange.put(VOLUME, true);
		valueIsChange.put(PRESET_NAME_LIST, true);
	}
	
	public HashMap<String, Boolean> getValueIsChange() {
		return valueIsChange;
	}

	public boolean isMute() {
		return mute;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
		valueIsChange.put(MUTE, true);
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
		valueIsChange.put(VOLUME, true);
	}
	
	public String getPresetNameList() {
		return presetNameList;
	}

	public void setPresetNameList(String presetNameList) {
		this.presetNameList = presetNameList;
		valueIsChange.put(PRESET_NAME_LIST, true);
	}

	@Override
	public String toString() {
		return valueIsChange.toString() + mute + "," + volume + "," + presetNameList;
	}
	
}

