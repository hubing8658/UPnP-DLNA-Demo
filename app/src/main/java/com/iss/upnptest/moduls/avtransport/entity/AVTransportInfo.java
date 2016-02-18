package com.iss.upnptest.moduls.avtransport.entity;

import java.util.HashMap;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;

/**
 * @author hubing
 * @version 1.0.0 2015-5-6
 */

public class AVTransportInfo {

	// 保存值的更改状态
	private HashMap<String, Boolean> valueIsChange = new HashMap<String, Boolean>();

	public static final String TRANSPORT_STATE = "TransportState";
	public static final String TRANSPORT_STATUS = "TransportStatus";
	public static final String NUMBER_Of_TRACKS = "NumberOfTracks";
	public static final String CURRENT_TRACK = "CurrentTrack";
	public static final String CURRENT_TRACK_DURATION = "CurrentTrackDuration";
	public static final String CURRENT_MEDIA_DURATION = "CurrentMediaDuration";
	public static final String CURRENT_TRACK_URI = "CurrentTrackURI";
	public static final String AV_TRANSPORT_URI = "AVTransportURI";
	public static final String CURRENT_TRACK_METADATA = "CurrentTrackMetaData";
	public static final String CURRENT_TRANSPORT_ACTIONS = "CurrentTransportActions";
	
	// 传输状态(播放、暂停等)
	private String transportState;
	// 传输过程状态
	private String transportStatus;
	private UnsignedIntegerFourBytes numberOfTracks;
	private UnsignedIntegerFourBytes currentTrack;
	private String currentTrackDuration;
	private String currentMediaDuration;
	private String currentTrackURI;
	private String AVTransportURI;
	private String currentTrackMetaData;
	private String currentTransportActions;
	
	public AVTransportInfo() {
		valueIsChange.put(TRANSPORT_STATE, false);
		valueIsChange.put(TRANSPORT_STATUS, false);
		valueIsChange.put(NUMBER_Of_TRACKS, false);
		valueIsChange.put(CURRENT_TRACK, false);
		valueIsChange.put(CURRENT_TRACK_DURATION, false);
		valueIsChange.put(CURRENT_MEDIA_DURATION, false);
		valueIsChange.put(CURRENT_TRACK_URI, false);
		valueIsChange.put(AV_TRANSPORT_URI, false);
		valueIsChange.put(CURRENT_TRACK_METADATA, false);
		valueIsChange.put(CURRENT_TRANSPORT_ACTIONS, false);
	}

	public AVTransportInfo(HashMap<String, Boolean> valueIsChange,
			String transportState, String transportStatus,
			UnsignedIntegerFourBytes numberOfTracks,
			UnsignedIntegerFourBytes currentTrack, String currentTrackDuration,
			String currentMediaDuration, String currentTrackURI,
			String aVTransportURI, String currentTrackMetaData,
			String currentTransportActions) {
		this.valueIsChange = valueIsChange;
		this.transportState = transportState;
		this.transportStatus = transportStatus;
		this.numberOfTracks = numberOfTracks;
		this.currentTrack = currentTrack;
		this.currentTrackDuration = currentTrackDuration;
		this.currentMediaDuration = currentMediaDuration;
		this.currentTrackURI = currentTrackURI;
		AVTransportURI = aVTransportURI;
		this.currentTrackMetaData = currentTrackMetaData;
		this.currentTransportActions = currentTransportActions;
		valueIsChange.put(TRANSPORT_STATE, true);
		valueIsChange.put(TRANSPORT_STATUS, true);
		valueIsChange.put(NUMBER_Of_TRACKS, true);
		valueIsChange.put(CURRENT_TRACK, true);
		valueIsChange.put(CURRENT_TRACK_DURATION, true);
		valueIsChange.put(CURRENT_MEDIA_DURATION, true);
		valueIsChange.put(CURRENT_TRACK_URI, true);
		valueIsChange.put(AV_TRANSPORT_URI, true);
		valueIsChange.put(CURRENT_TRACK_METADATA, true);
		valueIsChange.put(CURRENT_TRANSPORT_ACTIONS, true);
	}

	public HashMap<String, Boolean> getValueIsChange() {
		return valueIsChange;
	}

	public String getTransportState() {
		return transportState;
	}

	public void setTransportState(String transportState) {
		this.transportState = transportState;
		valueIsChange.put(TRANSPORT_STATE, true);
	}

	public String getTransportStatus() {
		return transportStatus;
	}

	public void setTransportStatus(String transportStatus) {
		this.transportStatus = transportStatus;
		valueIsChange.put(TRANSPORT_STATUS, true);
	}

	public UnsignedIntegerFourBytes getNumberOfTracks() {
		return numberOfTracks;
	}

	public void setNumberOfTracks(UnsignedIntegerFourBytes numberOfTracks) {
		this.numberOfTracks = numberOfTracks;
		valueIsChange.put(NUMBER_Of_TRACKS, true);
	}

	public UnsignedIntegerFourBytes getCurrentTrack() {
		return currentTrack;
	}

	public void setCurrentTrack(UnsignedIntegerFourBytes currentTrack) {
		this.currentTrack = currentTrack;
		valueIsChange.put(CURRENT_TRACK, true);
	}

	public String getCurrentTrackDuration() {
		return currentTrackDuration;
	}

	public void setCurrentTrackDuration(String currentTrackDuration) {
		this.currentTrackDuration = currentTrackDuration;
		valueIsChange.put(CURRENT_TRACK_DURATION, true);
	}

	public String getCurrentMediaDuration() {
		return currentMediaDuration;
	}

	public void setCurrentMediaDuration(String currentMediaDuration) {
		this.currentMediaDuration = currentMediaDuration;
		valueIsChange.put(CURRENT_MEDIA_DURATION, true);
	}

	public String getCurrentTrackURI() {
		return currentTrackURI;
	}

	public void setCurrentTrackURI(String currentTrackURI) {
		this.currentTrackURI = currentTrackURI;
		valueIsChange.put(CURRENT_TRACK_URI, true);
	}

	public String getAVTransportURI() {
		return AVTransportURI;
	}

	public void setAVTransportURI(String aVTransportURI) {
		AVTransportURI = aVTransportURI;
		valueIsChange.put(AV_TRANSPORT_URI, true);
	}

	public String getCurrentTrackMetaData() {
		return currentTrackMetaData;
	}

	public void setCurrentTrackMetaData(String currentTrackMetaData) {
		this.currentTrackMetaData = currentTrackMetaData;
		valueIsChange.put(CURRENT_TRACK_METADATA, true);
	}

	public String getCurrentTransportActions() {
		return currentTransportActions;
	}

	public void setCurrentTransportActions(String currentTransportActions) {
		this.currentTransportActions = currentTransportActions;
		valueIsChange.put(CURRENT_TRANSPORT_ACTIONS, true);
	}

	@Override
	public String toString() {
		return valueIsChange.toString();
	}
	
}
