package com.iss.upnptest.moduls.contentbrowse.callback;

import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;

/**
 * @author hubing
 * @version 1.0.0 2015-4-17
 */

public abstract class ContentBrowseActionCallback extends Browse {

	public ContentBrowseActionCallback(Service service, Container container) {
		super(service, container.getId(), BrowseFlag.DIRECT_CHILDREN, "*", 0,
				null, new SortCriterion(true, "dc:title"));
	}

	@Override
	public void updateStatus(Status status) {
	}

}
