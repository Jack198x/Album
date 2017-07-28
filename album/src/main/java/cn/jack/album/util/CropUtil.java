package cn.jack.album.util;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.io.File;

import cn.jack.album.CropFragment;
import cn.jack.album.PictureModel;
import cn.jack.album.R;

/**
 * Created by Jack on 2017/7/28.
 */

public class CropUtil {

    private static final String TAG = "CropFragment";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void crop(Activity activity, String authority, File cropInputFile, File cropOutputFile, CropListener cropListener) {
        if (PermissionUtil.checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            openCrop(activity, authority, cropInputFile, cropOutputFile, cropListener);
        } else {
            PermissionUtil.requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            Toast.makeText(activity, R.string.no_storage_permission, Toast.LENGTH_SHORT).show();
        }
    }


    private static void openCrop(Activity activity, String authority, File cropInputFile, File cropOutputFile, CropListener cropListener) {
        try {
            Intent intent = IntentUtil.getCropIntent(activity, authority, cropInputFile, cropOutputFile);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                CropFragment fragment = new CropFragment();
                fragment.setCropListener(cropListener);
                fragment.setCropOutputFile(cropOutputFile);
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .add(fragment, TAG)
                        .commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
                fragment.startActivityForResult(intent, Code.REQUEST_CROP);
            } else {
                Toast.makeText(activity, "图片打开失败!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "图片打开失败", Toast.LENGTH_SHORT).show();
        }
    }


    public interface CropListener {
        void onCrop(PictureModel cropPicture);
    }


}
