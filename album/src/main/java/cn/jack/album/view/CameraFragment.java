package cn.jack.album.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import java.io.File;

import cn.jack.album.model.PictureModel;
import cn.jack.album.util.CameraUtil;
import cn.jack.album.util.Code;
import cn.jack.album.util.MimeType;

/**
 * Created by Jack on 2017/7/28.
 */
public class CameraFragment extends Fragment {

    private CameraUtil.CameraListener cameraListener;
    private File cameraOutputFile;

    public CameraFragment() {

    }

    public void setCameraListener(CameraUtil.CameraListener cameraListener) {
        this.cameraListener = cameraListener;
    }

    public void setCameraOutputFile(File cameraOutputFile) {
        this.cameraOutputFile = cameraOutputFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Code.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    cameraListener.onCamera(new PictureModel(cameraOutputFile.getPath(), MimeType.JPEG.toString()));
                }
            });
        }
    }
}
