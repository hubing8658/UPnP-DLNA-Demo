package com.iss.upnptest.server.medialserver.dao;

import java.util.ArrayList;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.Res;
import org.seamless.util.MimeType;
import com.iss.upnptest.server.medialserver.entity.ContentTree;
import com.iss.upnptest.server.medialserver.entity.ImageItem;
import com.iss.upnptest.server.medialserver.entity.MItem;
import com.iss.upnptest.server.medialserver.entity.MusicTrack;
import com.iss.upnptest.server.medialserver.entity.VideoItem;
import com.iss.upnptest.utils.DurationUtil;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Images;

/**
 * @author hubing
 * @version 1.0.0 2015-5-8
 */

public class MediaContentDao {
	
	private static String resAddress;
	private static ContentResolver cr;
	
	public MediaContentDao(Context ctx, String serverAdd) {
		cr = ctx.getContentResolver();
		resAddress = "http://" + serverAdd + "/";
	}

	public ArrayList<MItem> getVideoItems() {
		ArrayList<MItem> items = new ArrayList<MItem>();
		
		String[] videoColumns = {Video.Media._ID,
				Video.Media.TITLE,
				Video.Media.DATA,
				Video.Media.ARTIST,
				Video.Media.MIME_TYPE,
				Video.Media.SIZE,
				Video.Media.DURATION,
				Video.Media.RESOLUTION};
		
		Cursor cur = cr.query(Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null);
		
		if (cur == null) {
			return items;
		}
		
		while (cur.moveToNext()) {
			String id = ContentTree.VIDEO_PREFIX + cur.getInt(cur.getColumnIndex(Video.Media._ID));
			String title = cur.getString(cur.getColumnIndex(Video.Media.TITLE));
			String filePath = cur.getString(cur.getColumnIndex(Video.Media.DATA));
			String creator = cur.getString(cur.getColumnIndex(Video.Media.ARTIST));
			String mimeType = cur.getString(cur.getColumnIndex(Video.Media.MIME_TYPE));
			long size = cur.getLong(cur.getColumnIndex(Video.Media.SIZE));
			long duration = cur.getLong(cur.getColumnIndex(Video.Media.DURATION));
			String resolution = cur.getString(cur.getColumnIndex(Video.Media.RESOLUTION));
			
			Res res = new Res(new MimeType(mimeType.substring(0, mimeType.indexOf('/')),
					mimeType.substring(mimeType.indexOf('/') + 1)), size, resAddress + id);
			res.setDuration(DurationUtil.toMilliTimeString(duration));
			res.setResolution(resolution);
			VideoItem videoItem = new VideoItem(id, ContentTree.VIDEO_ID, title, creator, filePath, res);
			items.add(videoItem);
		}
		return items;
	}
	
	public ArrayList<MItem> getAudioItems() {
		ArrayList<MItem> items = new ArrayList<MItem>();
		
		String[] audioColumns = { Audio.Media._ID,
				Audio.Media.TITLE,
				Audio.Media.DATA,
				Audio.Media.ARTIST,
				Audio.Media.MIME_TYPE,
				Audio.Media.SIZE,
				Audio.Media.DURATION,
				Audio.Media.ALBUM };
		
		Cursor cur = cr.query(Audio.Media.EXTERNAL_CONTENT_URI, audioColumns, null, null, null);
		
		if (cur == null) {
			return items;
		}
		
		while (cur.moveToNext()) {
			String id = ContentTree.AUDIO_PREFIX + cur.getInt(cur.getColumnIndex(Audio.Media._ID));
			String title = cur.getString(cur.getColumnIndex(Audio.Media.TITLE));
			String filePath = cur.getString(cur.getColumnIndex(Audio.Media.DATA));
			String creator = cur.getString(cur.getColumnIndex(Audio.Media.ARTIST));
			String mimeType = cur.getString(cur.getColumnIndex(Audio.Media.MIME_TYPE));
			long size = cur.getLong(cur.getColumnIndex(Audio.Media.SIZE));
			long duration = cur.getLong(cur.getColumnIndex(Audio.Media.DURATION));
			String album = cur.getString(cur.getColumnIndex(Audio.Media.ALBUM));
			
			Res res = new Res(new MimeType(mimeType.substring(0, mimeType.indexOf('/')),
					mimeType.substring(mimeType.indexOf('/') + 1)), size, resAddress + id);
			res.setDuration(DurationUtil.toMilliTimeString(duration));
			MusicTrack musicTrack = new MusicTrack(id, ContentTree.AUDIO_ID, title, creator, album,
					new PersonWithRole(creator, "Performer"), filePath, res);
			items.add(musicTrack);
		}
		
		return items;
	}
	
	public ArrayList<MItem> getImageItems() {
		ArrayList<MItem> items = new ArrayList<MItem>();
		
		String[] imageColumns = { Images.Media._ID,
				Images.Media.TITLE,
				Images.Media.DATA,
				Images.Media.MIME_TYPE,
				Images.Media.SIZE };
		
		Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, null);
		
		if (cur == null) {
			return items;
		}
		
		while (cur.moveToNext()) {
			String id = ContentTree.IMAGE_PREFIX + cur.getInt(cur.getColumnIndex(MediaStore.Images.Media._ID));
			String title = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.TITLE));
			String creator = "unkown";
			String filePath = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
			String mimeType = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
			long size = cur.getLong(cur
					.getColumnIndex(MediaStore.Images.Media.SIZE));

			Res res = new Res(new MimeType(mimeType.substring(0, mimeType.indexOf('/')),
					mimeType.substring(mimeType.indexOf('/') + 1)), size, resAddress + id);
			ImageItem imageItem = new ImageItem(id, ContentTree.IMAGE_ID,
					title, creator, filePath, res);
			items.add(imageItem);
		}
		
		return items;
	}
	
}

