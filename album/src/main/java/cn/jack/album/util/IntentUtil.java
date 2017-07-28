package cn.jack.album.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Jack on 2017/7/27.
 */

public class IntentUtil {

    private static final String OUTPUT_FORMAT = Bitmap.CompressFormat.JPEG.toString();//设置裁剪输出格式
    private static final int DEFAULT_ASPECT = 1;//设置裁剪宽高比
    private static final int DEFAULT_OUTPUT = 400;//设置输出图片分辨率


    public static Intent getCameraIntent(Activity activity, String authority, File outputFile) {
        Intent intent = new Intent();
        //通过FileProvider创建一个content类型的Uri
        Uri cameraOutPutUri = FileProviderCompat.getUriForFile(activity, authority, outputFile);
        FileProviderCompat.grantReadUriPermission(intent);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutPutUri);
        return intent;
    }


    public static Intent getCropIntent(Activity activity, String authority, File cropInputFile, File cropOutputFile) {
        Intent intent = new Intent();
        Uri inputUri = FileProviderCompat.getUriForFile(activity, authority, cropInputFile);
        FileProviderCompat.grantReadUriPermission(intent);
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        // 华为特殊处理 不然会显示圆
        if (android.os.Build.MANUFACTURER.contains("HUAWEI")) {
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);

        } else {
            intent.putExtra("aspectX", DEFAULT_ASPECT);
            intent.putExtra("aspectY", DEFAULT_ASPECT);

        }
        intent.putExtra("outputX", DEFAULT_OUTPUT);
        intent.putExtra("outputY", DEFAULT_OUTPUT);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", OUTPUT_FORMAT);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropOutputFile));
        return intent;
    }


}
