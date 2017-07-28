package cn.jack.album.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by Jack on 2017/1/22.
 */

public class FileProviderCompat {

    public static Uri getUriForFile(Context context, String authority, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static Uri getUriForFile(Context context, String authority, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, authority, new File(uri.getPath()));
        } else {
            return uri;
        }
    }

    public static void grantReadUriPermission(Intent intent) {
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }
}
