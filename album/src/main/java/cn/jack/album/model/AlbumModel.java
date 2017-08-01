package cn.jack.album.model;

import android.net.Uri;

/**
 * Created by Jack on 2015/10/20.
 */
public class AlbumModel {

    private String albumName = "";
    private String albumId = "";
    private int photoCount = 1;
    private Uri coverUri = null;


    public AlbumModel(String albumName, String albumId, Uri coverUri) {
        this.albumName = albumName;
        this.albumId = albumId;
        this.coverUri = coverUri;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    public Uri getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(Uri coverUri) {
        this.coverUri = coverUri;
    }
}
