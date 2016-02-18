package com.iss.upnptest.moduls.contentbrowse.ui;

import java.util.List;
import org.fourthline.cling.support.model.item.Item;
import android.content.Context;
import com.iss.upnptest.R;
import com.iss.upnptest.adapter.GeneralAdapter;
import com.iss.upnptest.moduls.contentbrowse.entity.ContentItem;
import com.iss.upnptest.utils.FiletypeUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public class ContentAdapter extends GeneralAdapter<ContentItem> {

	public ContentAdapter(Context ctx, int resource, List<ContentItem> data) {
		super(ctx, resource, data);
	}

	@Override
	public void convert(GeneralAdapter.ViewHolder holder, ContentItem item,
			int position) {
		String contentTitle;
		if (item.isContainer()) {
			contentTitle = item.getContainer().getTitle();
		} else {
			Item it = item.getItem();
			contentTitle = it.getTitle();
			switch (item.getFiletype()) {
			case FiletypeUtil.FILETYPE_MOVIE:
				holder.setImageResource(R.id.iv_icon,
						R.drawable.file_video_icon);
				break;
			case FiletypeUtil.FILETYPE_MUSIC:
				holder.setImageResource(R.id.iv_icon,
						R.drawable.file_audio_icon);
				break;
			case FiletypeUtil.FILETYPE_PIC:
				holder.setImageResource(R.id.iv_icon,
						R.drawable.file_image_icon);
				break;
			}
		}
		holder.setText(R.id.tv_title, contentTitle);
	}

}
