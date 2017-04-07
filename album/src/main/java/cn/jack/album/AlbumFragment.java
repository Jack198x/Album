package cn.jack.album;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Jack on 2017/4/6.
 */

public class AlbumFragment extends Fragment {

    private AlbumListener albumListener;

    public AlbumFragment() {

    }

    public void setAlbumListener(AlbumListener albumListener) {
        this.albumListener = albumListener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleActivityResult(requestCode, resultCode, data);
    }


    private void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Album.REQUEST_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                albumListener.onCameraFinish();
            }
        }
        if (requestCode == Album.REQUEST_CODE_ALBUM && resultCode == Album.RESULT_OK) {
            ArrayList<String> photos = data.getStringArrayListExtra("selectedPhotos");
            if (photos.size() > 0) {
                String path = photos.get(0);
                if (TextUtils.isEmpty(path)) {
                    albumListener.onError("所选图片路径错误");
                } else {
                    albumListener.onPhotosSelected(photos);
                }
            } else {
                albumListener.onError("未选择图片");
            }
        }
    }

}
