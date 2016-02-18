package com.iss.upnptest.server.medialserver.entity;

import java.util.Arrays;
import java.util.List;
import org.fourthline.cling.support.model.DIDLObject.Property.DC;
import org.fourthline.cling.support.model.DIDLObject.Property.UPNP;
import org.fourthline.cling.support.model.Person;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.StorageMedium;
import org.fourthline.cling.support.model.container.Container;


/**
 * @author hubing
 * @version 1.0.0 2015-5-8
 */

public class ImageItem extends MItem {

	public static final Class CLASS = new Class("object.item.imageItem");

    public ImageItem() {
        setClazz(CLASS);
    }

    public ImageItem(MItem other) {
        super(other);
    }

    public ImageItem(String id, Container parent, String title, String creator, String filePath, Res... resource) {
        this(id, parent.getId(), title, creator, filePath, resource);
    }

    public ImageItem(String id, String parentID, String title, String creator, String filePath, Res... resource) {
        super(id, parentID, title, creator, filePath, CLASS);
        if (resource != null) {
            getResources().addAll(Arrays.asList(resource));
        }
    }

    public String getDescription() {
        return getFirstPropertyValue(DC.DESCRIPTION.class);
    }

    public ImageItem setDescription(String description) {
        replaceFirstProperty(new DC.DESCRIPTION(description));
        return this;
    }

    public String getLongDescription() {
        return getFirstPropertyValue(UPNP.LONG_DESCRIPTION.class);
    }

    public ImageItem setLongDescription(String description) {
        replaceFirstProperty(new UPNP.LONG_DESCRIPTION(description));
        return this;
    }

    public Person getFirstPublisher() {
        return getFirstPropertyValue(DC.PUBLISHER.class);
    }

    public Person[] getPublishers() {
        List<Person> list = getPropertyValues(DC.PUBLISHER.class);
        return list.toArray(new Person[list.size()]);
    }

    public ImageItem setPublishers(Person[] publishers) {
        removeProperties(DC.PUBLISHER.class);
        for (Person publisher : publishers) {
            addProperty(new DC.PUBLISHER(publisher));
        }
        return this;
    }

    public StorageMedium getStorageMedium() {
        return getFirstPropertyValue(UPNP.STORAGE_MEDIUM.class);
    }

    public ImageItem setStorageMedium(StorageMedium storageMedium) {
        replaceFirstProperty(new UPNP.STORAGE_MEDIUM(storageMedium));
        return this;
    }

    public String getRating() {
        return getFirstPropertyValue(UPNP.RATING.class);
    }

    public ImageItem setRating(String rating) {
        replaceFirstProperty(new UPNP.RATING(rating));
        return this;
    }

    public String getDate() {
        return getFirstPropertyValue(DC.DATE.class);
    }

    public ImageItem setDate(String date) {
        replaceFirstProperty(new DC.DATE(date));
        return this;
    }

    public String getFirstRights() {
        return getFirstPropertyValue(DC.RIGHTS.class);
    }

    public String[] getRights() {
        List<String> list = getPropertyValues(DC.RIGHTS.class);
        return list.toArray(new String[list.size()]);
    }

    public ImageItem setRights(String[] rights) {
        removeProperties(DC.RIGHTS.class);
        for (String right : rights) {
            addProperty(new DC.RIGHTS(right));
        }
        return this;
    }
	
}

