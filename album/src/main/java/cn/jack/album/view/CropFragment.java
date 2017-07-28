package cn.jack.album.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import java.io.File;

import cn.jack.album.model.PictureModel;
import cn.jack.album.util.Code;
import cn.jack.album.util.CropUtil;

/**
 * Created by Jack on 2017/7/28.
 */
public class CropFragment extends Fragment {

    private CropUtil.CropListener cropListener;
    private File cropOutputFile;

    public CropFragment() {

    }

    public void setCropListener(CropUtil.CropListener cropListener) {
        this.cropListener = cropListener;
    }

    public void setCropOutputFile(File cropOutputFile) {
        this.cropOutputFile = cropOutputFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Code.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            final PictureModel cropPicture = new PictureModel();
            cropPicture.setUri(Uri.fromFile(cropOutputFile));
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    cropListener.onCrop(cropPicture);
                }
            });
        }
    }
}
