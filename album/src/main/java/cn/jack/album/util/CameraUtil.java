package cn.jack.album.util;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;

import cn.jack.album.CameraFragment;
import cn.jack.album.PictureModel;
import cn.jack.album.R;

/**
 * Created by Jack on 2017/7/28.
 */

public class CameraUtil {

    private static final String TAG = "CameraFragment";

    public static void camera(Activity activity, String authority, File cameraOutputFile, CameraListener cameraListener) {
        if (PermissionUtil.checkPermission(activity, Manifest.permission.CAMERA)) {
            openCamera(activity, authority, cameraOutputFile, cameraListener);
        } else {
            PermissionUtil.requestPermission(activity, Manifest.permission.CAMERA);
            Toast.makeText(activity, R.string.no_camera_permission, Toast.LENGTH_SHORT).show();
        }
    }


    private static void openCamera(Activity activity, String authority, File cameraOutputFile, CameraListener cameraListener) {
        try {
            Intent intent = IntentUtil.getCameraIntent(activity, authority, cameraOutputFile);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                CameraFragment fragment = new CameraFragment();
                fragment.setCameraListener(cameraListener);
                fragment.setCameraOutputFile(cameraOutputFile);
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .add(fragment, TAG)
                        .commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
                fragment.startActivityForResult(intent, Code.REQUEST_CAMERA);
            } else {
                Toast.makeText(activity, "无法打开相机应用!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "无法打开相机应用!", Toast.LENGTH_SHORT).show();
        }
    }

    public interface CameraListener {
        void onCamera(PictureModel cameraPicture);
    }
}
