package cn.jack.album.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContentResolverCompat;

import cn.jack.album.data.AlbumData;
import cn.jack.album.data.PictureData;
import cn.jack.album.model.AlbumModel;
import cn.jack.album.model.PictureModel;

/**
 * Created by Jack on 2017/7/31.
 */

public class AlbumLoader {

    public static final String ALBUM_ID_ALL_IMAGES = "ALBUM_ID_ALL_IMAGES";
    private static final Uri QUERY_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final String[] PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.ImageColumns.ORIENTATION};
    private static final String SELECTION_ALBUM = MediaStore.Images.Media.BUCKET_ID + " = ?";
    private static final String ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC";

    private Context context;

    public AlbumLoader(Context context) {
        this.context = context;
    }


    public void load(String albumId) {
        Cursor cursor = null;
        try {
            boolean loadAllImages = albumId.equals(ALBUM_ID_ALL_IMAGES);
            String[] selectionArgs = loadAllImages ? null : new String[]{albumId};
            cursor = getPictureCursor(selectionArgs);
            if (cursor != null) {
                PictureData.getInstance().clear();
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID));
                    String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                    PictureData.getInstance().add(new PictureModel(path, mimeType));
                    AlbumData.getInstance().add(new AlbumModel(albumName, id, Uri.parse("file://" + path)));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private Cursor getPictureCursor(@Nullable String[] selectionArgs) {
        if (selectionArgs != null) {
            return ContentResolverCompat.query(context.getContentResolver(),
                    QUERY_URI, PROJECTION, SELECTION_ALBUM, selectionArgs, ORDER_BY, null);
        } else {
            return ContentResolverCompat.query(context.getContentResolver(),
                    QUERY_URI, PROJECTION, null, null, ORDER_BY, null);
        }
    }


}
