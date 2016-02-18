package com.iss.upnptest.server.medialserver.entity;

import java.util.List;
import org.fourthline.cling.support.model.DIDLObject.Property.DC;
import org.fourthline.cling.support.model.DIDLObject.Property.UPNP;
import org.fourthline.cling.support.model.Person;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.StorageMedium;
import org.fourthline.cling.support.model.container.Container;

/**
 * @author hubing
 * @version 1.0.0 2015-5-8
 */

public class MusicTrack extends AudioItem {

	public static final Class CLASS = new Class("object.item.audioItem.musicTrack");

    public MusicTrack() {
        setClazz(CLASS);
    }

    public MusicTrack(MItem other) {
        super(other);
    }

    public MusicTrack(String id, Container parent, String title, String creator, String album, String artist, String filePath, Res... resource) {
        this(id, parent.getId(), title, creator, album, artist, filePath, resource);
    }

    public MusicTrack(String id, Container parent, String title, String creator, String album, PersonWithRole artist, String filePath, Res... resource) {
        this(id, parent.getId(), title, creator, album, artist, filePath, resource);
    }

    public MusicTrack(String id, String parentID, String title, String creator, String album, String artist, String filePath, Res... resource) {
        this(id, parentID, title, creator, album, new PersonWithRole(artist), filePath, resource);
    }

    public MusicTrack(String id, String parentID, String title, String creator, String album, PersonWithRole artist, String filePath, Res... resource) {
        super(id, parentID, title, creator, filePath, resource);
        setClazz(CLASS);
        if (album != null) {
        	setAlbum(album);
        }
        if (artist != null) {
        	addProperty(new UPNP.ARTIST(artist));
        }
    }

    public PersonWithRole getFirstArtist() {
        return getFirstPropertyValue(UPNP.ARTIST.class);
    }

    public PersonWithRole[] getArtists() {
        List<PersonWithRole> list = getPropertyValues(UPNP.ARTIST.class);
        return list.toArray(new PersonWithRole[list.size()]);
    }

    public MusicTrack setArtists(PersonWithRole[] artists) {
        removeProperties(UPNP.ARTIST.class);
        for (PersonWithRole artist : artists) {
            addProperty(new UPNP.ARTIST(artist));
        }
        return this;
    }

    public String getAlbum() {
        return getFirstPropertyValue(UPNP.ALBUM.class);
    }

    public MusicTrack setAlbum(String album) {
        replaceFirstProperty(new UPNP.ALBUM(album));
        return this;
    }

    public Integer getOriginalTrackNumber() {
        return getFirstPropertyValue(UPNP.ORIGINAL_TRACK_NUMBER.class);
    }

    public MusicTrack setOriginalTrackNumber(Integer number) {
        replaceFirstProperty(new UPNP.ORIGINAL_TRACK_NUMBER(number));
        return this;
    }

    public String getFirstPlaylist() {
        return getFirstPropertyValue(UPNP.PLAYLIST.class);
    }

    public String[] getPlaylists() {
        List<String> list = getPropertyValues(UPNP.PLAYLIST.class);
        return list.toArray(new String[list.size()]);
    }

    public MusicTrack setPlaylists(String[] playlists) {
        removeProperties(UPNP.PLAYLIST.class);
        for (String s : playlists) {
            addProperty(new UPNP.PLAYLIST(s));
        }
        return this;
    }

    public StorageMedium getStorageMedium() {
        return getFirstPropertyValue(UPNP.STORAGE_MEDIUM.class);
    }

    public MusicTrack setStorageMedium(StorageMedium storageMedium) {
        replaceFirstProperty(new UPNP.STORAGE_MEDIUM(storageMedium));
        return this;
    }

    public Person getFirstContributor() {
        return getFirstPropertyValue(DC.CONTRIBUTOR.class);
    }

    public Person[] getContributors() {
        List<Person> list = getPropertyValues(DC.CONTRIBUTOR.class);
        return list.toArray(new Person[list.size()]);
    }

    public MusicTrack setContributors(Person[] contributors) {
        removeProperties(DC.CONTRIBUTOR.class);
        for (Person p : contributors) {
            addProperty(new DC.CONTRIBUTOR(p));
        }
        return this;
    }

    public String getDate() {
        return getFirstPropertyValue(DC.DATE.class);
    }

    public MusicTrack setDate(String date) {
        replaceFirstProperty(new DC.DATE(date));
        return this;
    }
    
}

