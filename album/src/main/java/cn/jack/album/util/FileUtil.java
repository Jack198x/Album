package cn.jack.album.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Jack on 2017/2/9.
 */

public class FileUtil {
    public static File getSystemPicturePath() {
        File systemPicturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!systemPicturePath.exists()) {
            boolean result = systemPicturePath.mkdirs();
        }
        return systemPicturePath;
    }
}
