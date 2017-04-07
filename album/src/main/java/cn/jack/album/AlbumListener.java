package cn.jack.album;

import java.util.ArrayList;

/**
 * Created by Jack on 2015/10/20.
 */
public interface AlbumListener {


    void onCameraFinish();

    /**
     *
     * @param photos
     */
    void onPhotosSelected(ArrayList<String> photos);

    void onError(String error);

}
