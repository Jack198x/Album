package cn.jack.album;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

/**
 * Created by Jack on 2017/7/27.
 */

public class MediaScannerUtil {


    public static void scanFile(Context context, Uri scanUri, MediaScannerConnection.OnScanCompletedListener callback) {
        MediaScannerConnection.scanFile(context, new String[]{scanUri.getPath()}, null, callback);
    }

    public static void scanFile(Context context, File file, MediaScannerConnection.OnScanCompletedListener callback) {
        MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, callback);
    }


    /**
     * 通知媒体库扫描刷新当前文件路径
     *
     * @param context
     * @param paths
     * @param callback
     */
    private static void scanFile(Context context, String[] paths, MediaScannerConnection.OnScanCompletedListener callback) {
        MediaScannerConnection.scanFile(context, paths, null, callback);
    }



}
