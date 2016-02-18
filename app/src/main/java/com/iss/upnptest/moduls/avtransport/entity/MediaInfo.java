package com.iss.upnptest.moduls.avtransport.entity;

import java.util.Map;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.model.RecordMediumWriteStatus;
import org.fourthline.cling.support.model.StorageMedium;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public class MediaInfo {

	private String currentURI = "";
	private String currentURIMetaData = "";
	private String nextURI = "NOT_IMPLEMENTED";
	private String nextURIMetaData = "NOT_IMPLEMENTED";

	private UnsignedIntegerFourBytes numberOfTracks = new UnsignedIntegerFourBytes(
			0);
	private String mediaDuration = "00:00:00";
	private StorageMedium playMedium = StorageMedium.NONE;
	private StorageMedium recordMedium = StorageMedium.NOT_IMPLEMENTED;
	private RecordMediumWriteStatus writeStatus = RecordMediumWriteStatus.NOT_IMPLEMENTED;

	public MediaInfo() {
	}

	public MediaInfo(Map<String, Object> args) {
		this((String) args.get("CurrentURI"), (String) args
				.get("CurrentURIMetaData"), (String) args.get("NextURI"),
				(String) args.get("NextURIMetaData"),

				(UnsignedIntegerFourBytes) args.get("NrTracks"), (String) args
						.get("MediaDuration"), StorageMedium
						.valueOrVendorSpecificOf((String) args
								.get("PlayMedium")), StorageMedium
						.valueOrVendorSpecificOf((String) args
								.get("RecordMedium")), RecordMediumWriteStatus
						.valueOrUnknownOf((String) args.get("WriteStatus")));
	}

	public MediaInfo(String currentURI, String currentURIMetaData,
			String nextURI, String nextURIMetaData,
			UnsignedIntegerFourBytes numberOfTracks, String mediaDuration,
			StorageMedium playMedium, StorageMedium recordMedium,
			RecordMediumWriteStatus writeStatus) {
		this.currentURI = currentURI;
		this.currentURIMetaData = currentURIMetaData;
		this.nextURI = nextURI;
		this.nextURIMetaData = nextURIMetaData;
		this.numberOfTracks = numberOfTracks;
		this.mediaDuration = mediaDuration;
		this.playMedium = playMedium;
		this.recordMedium = recordMedium;
		this.writeStatus = writeStatus;
	}

	public String getCurrentURI() {
		return currentURI;
	}

	public String getCurrentURIMetaData() {
		return currentURIMetaData;
	}

	public String getNextURI() {
		return nextURI;
	}

	public String getNextURIMetaData() {
		return nextURIMetaData;
	}

	public UnsignedIntegerFourBytes getNumberOfTracks() {
		return numberOfTracks;
	}

	public String getMediaDuration() {
		return mediaDuration;
	}

	public StorageMedium getPlayMedium() {
		return playMedium;
	}

	public StorageMedium getRecordMedium() {
		return recordMedium;
	}

	public RecordMediumWriteStatus getWriteStatus() {
		return writeStatus;
	}

}
