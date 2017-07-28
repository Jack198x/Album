package cn.jack.album;

import android.net.Uri;

import java.io.File;

/**
 * Created by Jack on 2017/7/27.
 */

public class PictureModel {

    private String id;
    private Uri uri;
    private File file;
    private int type = 1;
    private int orientation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public PictureType getType() {
        return type == 0 ? PictureType.CAMERA : PictureType.PICTURE;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
