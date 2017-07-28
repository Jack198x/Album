package cn.jack.album.view;

import android.app.Fragment;
import android.content.Intent;

import cn.jack.album.data.AlbumData;
import cn.jack.album.util.Code;

/**
 * Created by Jack on 2017/4/6.
 */

public class AlbumFragment extends Fragment {

    private PictureSelectListener selectListener;

    public AlbumFragment() {

    }

    public void setPictureSelectListener(PictureSelectListener selectListener) {
        this.selectListener = selectListener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleActivityResult(requestCode, resultCode, data);
    }


    private void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (selectListener == null) {
            return;
        }
        if (requestCode == Code.REQUEST_ALBUM && resultCode == Code.RESULT_OK) {
            selectListener.onPictureSelect(AlbumData.getInstance().getSelectedPictures());
        }
    }

}
