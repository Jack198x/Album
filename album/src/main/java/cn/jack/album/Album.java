package cn.jack.album;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

import cn.jack.album.data.AlbumData;
import cn.jack.album.util.Code;
import cn.jack.album.util.IntentUtil;
import cn.jack.album.util.PermissionUtil;

/**
 * Created by Jack on 2016/12/23.
 */

public class Album {

    private static final String TAG = "AlbumFragment";

    private Activity activity;
    private AlbumFragment fragment;


    private Album(Activity activity) {
        this.activity = activity;
        fragment = new AlbumFragment();
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(fragment, TAG)
                .commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public static Album with(Activity activity, String authority) {
        AlbumData.getInstance().init(authority);
        return new Album(activity);
    }

    public Album title(String title) {
        AlbumData.getInstance().setTitle(TextUtils.isEmpty(title) ? "选择图片" : title);
        return this;
    }

    public Album setListener(PictureSelectListener listener) {
        fragment.setPictureSelectListener(listener);
        return this;
    }

    public Album enableCamera(boolean enable) {
        AlbumData.getInstance().setEnableCamera(enable);
        return this;
    }

    public Album enableCrop(boolean enableCrop) {
        AlbumData.getInstance().setEnableCrop(enableCrop);
        return this;
    }


    /**
     * 设置图片可选上限
     * maxChoice 1为单选，大于1不支持裁剪
     */
    public Album maxChoice(int maxChoice) {
        AlbumData.getInstance().setMaxChoice(maxChoice);
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
     * 跳转到相册
     */
    private void startAlbumActivityForResult() {
        Intent intent = new Intent(activity, AlbumActivity.class);
        fragment.startActivityForResult(intent, Code.REQUEST_ALBUM);
    }
}
