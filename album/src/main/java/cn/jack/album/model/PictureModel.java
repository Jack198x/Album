package cn.jack.album.model;

import android.net.Uri;

import java.io.File;

import cn.jack.album.util.MimeType;

/**
 * Created by Jack on 2017/7/27.
 */

public class PictureModel {

    private String path;
    private String mimeType= MimeType.JPEG.toString();


    public PictureModel(String path, String mimeType) {
        this.path = path;
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public Uri getUri() {
        return Uri.parse("file://" + path);
    }


    public File getFile() {
        return new File(path);
    }


    public String getMimeType() {
        return mimeType;
    }

    public boolean isGif() {
        return mimeType.equals(MimeType.GIF.toString());
    }
}
