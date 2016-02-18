package com.iss.upnptest.moduls.contentbrowse.entity;

import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.model.DIDLObject;
import com.iss.upnptest.utils.FiletypeUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-4-16
 */

public class ContentItem {

	private Device device;
	private Service service;
	private DIDLObject content;
	private String id;
	private boolean isContainer;
	private int filetype; // isContainer为false时所属文件类型
	
	public ContentItem(Container container, Service service) {
		this.content = container;
		this.service = service;
		this.id = container.getId();
		isContainer = true;
		this.filetype = FiletypeUtil.FILETYPE_OTHER;
	}
	
	public ContentItem(Item item, Service service, int filetype) {
		this.content = item;
		this.service = service;
		this.id = item.getId();
		this.filetype = filetype;
		isContainer = false;
	}
	
	public Container getContainer() {
		if (isContainer) {
			return (Container)content;
		}
		return null;
	}
	
	public Item getItem() {
		if (!isContainer) {
			return (Item)content;
		}
		return null;
	}
	
	public Service getService() {
		return service;
	}
	
	public boolean isContainer() {
		return isContainer;
	}
	
	public int getFiletype() {
		return filetype;
	}

	public void setFiletype(int filetype) {
		if (isContainer()) {
			filetype = FiletypeUtil.FILETYPE_OTHER;
		}
		this.filetype = filetype;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ContentItem that = (ContentItem) o;
		if (!id.equals(that.id)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		return content.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(content.getTitle()).append("\n\n");
		if (!isContainer) {
			content.getFirstResource().getValue();
		}
		return super.toString();
	}
	
}

