package com.iss.upnptest.server.medialserver.entity;

import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

/**
 * @author hubing
 * @version 1.0.0 2015-5-8
 */

public class ContentNode {

	private Container container;
	private Item item;
	private String id;
	private String fullPath;
	private boolean isItem;

	public ContentNode(String id, Container container) {
		this.id = id;
		this.container = container;
		this.fullPath = null;
		this.isItem = false;
	}

	public ContentNode(String id, Item item, String fullPath) {
		this.id = id;
		this.item = item;
		this.fullPath = fullPath;
		this.isItem = true;
	}

	public String getId() {
		return id;
	}

	public Container getContainer() {
		return container;
	}

	public Item getItem() {
		return item;
	}

	public String getFullPath() {
		if (isItem && fullPath != null) {
			return fullPath;
		}
		return null;
	}

	public boolean isItem() {
		return isItem;
	}

}
