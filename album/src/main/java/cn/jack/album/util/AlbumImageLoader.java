package cn.jack.album.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * Created by Jack on 2016/9/28.
 */

public class AlbumImageLoader {




    public static RequestCreator with(Context context, String url) {
        return Picasso.with(context).load(TextUtils.isEmpty(url) ? "http://????" : url);
    }

    public static RequestCreator with(Context context, int resourceId) {
        return Picasso.with(context).load(resourceId);
    }

    public static RequestCreator with(Context context, Uri uri) {
        return Picasso.with(context).load(uri);
    }

    public static RequestCreator with(Context context, File file) {
        return Picasso.with(context).load(file);
    }
}
