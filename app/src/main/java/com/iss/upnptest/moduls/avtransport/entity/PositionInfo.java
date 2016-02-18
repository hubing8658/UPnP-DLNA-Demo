package com.iss.upnptest.moduls.avtransport.entity;

import java.util.Map;
import org.fourthline.cling.model.ModelUtil;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public class PositionInfo {

	private UnsignedIntegerFourBytes track = new UnsignedIntegerFourBytes(0);
	private String trackDuration = "00:00:00";
	private String trackMetaData = "NOT_IMPLEMENTED";
	private String trackURI = "";
	private String relTime = "00:00:00";
	private String absTime = "00:00:00"; // TODO: MORE VALUES IN DOMAIN!
	private int relCount = Integer.MAX_VALUE; // Indicates that we don't support
												// this
	private int absCount = Integer.MAX_VALUE;

	public PositionInfo() {
	}

	public PositionInfo(Map<String, Object> args) {
		this.track = (UnsignedIntegerFourBytes) args.get("Track");
		this.trackDuration = (String) args.get("TrackDuration");
		this.trackMetaData = (String) args.get("TrackMetaData");
		this.trackURI = (String) args.get("TrackURI");
		this.relTime = (String) args.get("RelTime");
		this.absTime = (String) args.get("AbsTime");
		this.relCount = (Integer) args.get("RelCount");
		this.absCount = (Integer) args.get("AbsCount");
	}

	public PositionInfo(long track, String trackDuration, String trackMetaData,
			String trackURI, String relTime, String absTime, int relCount,
			int absCount) {
		this.track = new UnsignedIntegerFourBytes(track);
		this.trackDuration = trackDuration;
		this.trackMetaData = trackMetaData;
		this.trackURI = trackURI;
		this.relTime = relTime;
		this.absTime = absTime;
		this.relCount = relCount;
		this.absCount = absCount;
	}

	public UnsignedIntegerFourBytes getTrack() {
		return track;
	}

	public String getTrackDuration() {
		return trackDuration;
	}

	public String getTrackMetaData() {
		return trackMetaData;
	}

	public String getTrackURI() {
		return trackURI;
	}

	public String getRelTime() {
		return relTime;
	}

	public String getAbsTime() {
		return absTime;
	}

	public int getRelCount() {
		return relCount;
	}

	public int getAbsCount() {
		return absCount;
	}

	public void setTrackDuration(String trackDuration) {
		this.trackDuration = trackDuration;
	}

	public void setRelTime(String relTime) {
		this.relTime = relTime;
	}

	public long getTrackDurationSeconds() {
		return getTrackDuration() == null ? 0 : ModelUtil
				.fromTimeString(getTrackDuration());
	}

	public long getTrackElapsedSeconds() {
		return getRelTime() == null || getRelTime().equals("NOT_IMPLEMENTED") ? 0
				: ModelUtil.fromTimeString(getRelTime());
	}

	public long getTrackRemainingSeconds() {
		return getTrackDurationSeconds() - getTrackElapsedSeconds();
	}

	public int getElapsedPercent() {
		long elapsed = getTrackElapsedSeconds();
		long total = getTrackDurationSeconds();
		if (elapsed == 0 || total == 0)
			return 0;
		return new Double(elapsed / ((double) total / 100)).intValue();
	}

	@Override
	public String toString() {
		return "(PositionInfo) Track: " + getTrack() + " RelTime: "
				+ getRelTime() + " Duration: " + getTrackDuration()
				+ " Percent: " + getElapsedPercent();
	}

}
