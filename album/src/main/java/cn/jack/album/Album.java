package cn.jack.album;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

import cn.jack.album.util.FileProviderCompat;
import cn.jack.album.util.PermissionUtil;

/**
 * Created by Jack on 2016/12/23.
 */

public class Album {

    public static final int REQUEST_CODE_ALBUM = 0x1111;
    public static final int REQUEST_CODE_CAMERA = 0x2222;
    public static final int REQUEST_CODE_CROP = 0x3333;
    public static final int RESULT_OK = 0x9999;
    public static final int RESULT_CANCEL = 0x9998;

    private static final String TAG = "AlbumFragment";

    private static final String OUTPUT_FORMAT = Bitmap.CompressFormat.JPEG.toString();//设置裁剪输出格式
    private static final int DEFAULT_ASPECT = 1;//设置裁剪宽高比
    private static final int DEFAULT_OUTPUT = 400;//设置输出图片分辨率

    private Activity activity;
    private AlbumFragment fragment;
    private String title;
    private boolean enableCamera = false;
    private boolean enableCrop = false;
    private int maxChoice = 1;

    public Album(Activity activity, String title, boolean enableCamera) {
        this.activity = activity;
        this.title = title;
        this.enableCamera = enableCamera;
        fragment = new AlbumFragment();
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(fragment, TAG)
                .commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public static Album with(Activity activity) {
        return new Album(activity, "选择图片", false);
    }


    public Album title(String title) {
        this.title = TextUtils.isEmpty(title) ? "选择图片" : title;
        return this;
    }

    public Album setListener(AlbumListener listener) {
        fragment.setAlbumListener(listener);
        return this;
    }

    public Album enableCamera(boolean enable) {
        this.enableCamera = enable;
        return this;
    }

    public Album enableCrop(boolean enableCrop) {
        this.enableCrop = maxChoice == 1 && enableCrop;
        return this;
    }


    /**
     * 设置图片可选上限
     * maxChoice 1为单选，大于1不支持裁剪
     */
    public Album maxChoice(int maxChoice) {
        this.maxChoice = maxChoice;
        if (maxChoice > 1) {
            this.enableCrop = false;
        }
        if (maxChoice < 1) {
            this.maxChoice = 1;
        }
        return this;
    }


    public void open() {
        if (PermissionUtil.checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            startAlbumActivityForResult();
        } else {
            PermissionUtil.requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            Toast.makeText(activity, R.string.no_storage_permission, Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 跳转到系统相机页面
     *
     * @param outputFile
     */
    public void openCamera(File outputFile) {
        if (PermissionUtil.checkPermission(activity, Manifest.permission.CAMERA)) {
            startCameraActivityForResult(outputFile);
        } else {
            PermissionUtil.requestPermission(activity, Manifest.permission.CAMERA);
            Toast.makeText(activity, R.string.no_camera_permission, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 跳转到系统裁剪界面
     *
     * @param cropInputFile
     * @param cropOutputUri
     */
    public void openCrop(File cropInputFile, Uri cropOutputUri) {
        if (PermissionUtil.checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            startCropActivityForResult(cropInputFile, cropOutputUri);
        } else {
            PermissionUtil.requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            Toast.makeText(activity, R.string.no_storage_permission, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转到系统裁剪界面
     *
     * @param cropInputUri
     * @param cropOutputUri
     */
    public void openCrop(Uri cropInputUri, Uri cropOutputUri) {
        if (PermissionUtil.checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            startCropActivityForResult(new File(cropInputUri.getPath()), cropOutputUri);
        } else {
            PermissionUtil.requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            Toast.makeText(activity, R.string.no_storage_permission, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 跳转到相册
     */
    private void startAlbumActivityForResult() {
        Intent intent = new Intent(activity, AlbumActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("enableCamera", enableCamera);
        intent.putExtra("enableCrop", enableCrop);
        intent.putExtra("maxLimit", maxChoice);
        fragment.startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }


    /**
     * 跳转到系统相机页面
     *
     * @param outputFile
     */
    private void startCameraActivityForResult(File outputFile) {
        try {
            Intent intent = new Intent();
            //通过FileProvider创建一个content类型的Uri
            Uri cameraOutPutUri = FileProviderCompat.getUriForFile(activity, activity.getString(R.string.support_authority), outputFile);
            FileProviderCompat.grantReadUriPermission(intent);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutPutUri);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                fragment.startActivityForResult(intent, REQUEST_CODE_CAMERA);
            } else {
                Toast.makeText(activity, "无法打开相机应用!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "无法打开相机应用!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 跳转到系统裁剪界面
     *
     * @param cropInputFile
     * @param cropOutputUri
     */
    private void startCropActivityForResult(File cropInputFile, Uri cropOutputUri) {
        try {
            Intent intent = new Intent();
            Uri inputUri = FileProviderCompat.getUriForFile(activity, activity.getString(R.string.support_authority), cropInputFile);
            FileProviderCompat.grantReadUriPermission(intent);
            intent.setAction("com.android.camera.action.CROP");
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra("crop", true);
            intent.putExtra("scale", true);
            intent.putExtra("aspectX", DEFAULT_ASPECT);
            intent.putExtra("aspectY", DEFAULT_ASPECT);
            intent.putExtra("outputX", DEFAULT_OUTPUT);
            intent.putExtra("outputY", DEFAULT_OUTPUT);
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", OUTPUT_FORMAT);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropOutputUri);
            fragment.startActivityForResult(intent, REQUEST_CODE_CROP);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "图片打开失败", Toast.LENGTH_SHORT).show();
        }
    }


}
