package com.iss.upnptest.server.medialserver.entity;

import java.util.ArrayList;
import java.util.List;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.DescMeta;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

/**
 * @author hubing
 * @version 1.0.0 2015-5-12
 */

public class MItem extends Item {

	private String filePath;

    public MItem() {
    }

    public MItem(Item other) {
        super(other);
        setRefID(other.getRefID());
    }

    public MItem(String id, String parentID, String title, String creator, boolean restricted, WriteStatus writeStatus, Class clazz, List<Res> resources, List<Property> properties, List<DescMeta> descMetadata) {
        super(id, parentID, title, creator, restricted, writeStatus, clazz, resources, properties, descMetadata);
    }

    public MItem(String id, String parentID, String title, String creator, boolean restricted, WriteStatus writeStatus, Class clazz, List<Res> resources, List<Property> properties, List<DescMeta> descMetadata, String refID) {
        super(id, parentID, title, creator, restricted, writeStatus, clazz, resources, properties, descMetadata);
        this.refID = refID;
    }

    public MItem(String id, Container parent, String title, String creator, DIDLObject.Class clazz) {
        this(id, parent.getId(), title, creator, false, null, clazz, new ArrayList(), new ArrayList(), new ArrayList());
    }

    public MItem(String id, Container parent, String title, String creator, DIDLObject.Class clazz, String refID) {
        this(id, parent.getId(), title, creator, false, null, clazz, new ArrayList(), new ArrayList(), new ArrayList(), refID);
    }

    public MItem(String id, String parentID, String title, String creator, String filePath, DIDLObject.Class clazz) {
        this(id, parentID, title, creator, false, null, clazz, new ArrayList(), new ArrayList(), new ArrayList());
        this.filePath = filePath;
    }

    public MItem(String id, String parentID, String title, String creator, DIDLObject.Class clazz, String refID) {
        this(id, parentID, title, creator, false, null, clazz, new ArrayList(), new ArrayList(), new ArrayList(), refID);
    }

    public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
    
}

