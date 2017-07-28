package cn.jack.album.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

import cn.jack.album.model.AlbumModel;
import cn.jack.album.model.PictureModel;


/**
 * Created by Jack on 2015/10/20.
 */
public class AlbumUtil {

    public static final String ALBUM_ID_ALL_PHOTOS = "all_photos";


    /**
     * 获取相册列表
     *
     * @param context
     * @return
     */
    public static ArrayList<AlbumModel> getAlbums(Context context) {
        ArrayList<AlbumModel> albums = new ArrayList<>();
        Cursor cursor = null;
        try {
            int totalPictureCount = getPicturesCount(context, ALBUM_ID_ALL_PHOTOS);
            if (totalPictureCount > 0) {
                AlbumModel allPictures = new AlbumModel("全部图片", ALBUM_ID_ALL_PHOTOS, getAlbumCover(context, ALBUM_ID_ALL_PHOTOS), totalPictureCount);
                albums.add(allPictures);
                final String[] columns = {MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.BUCKET_ID};
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns, null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                        String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID));
                        if (!isAlbumAdded(albums, id)) {
                            int count = getPicturesCount(context, id);
                            Uri coverUri = getAlbumCover(context, id);
                            albums.add(new AlbumModel(name, id, coverUri, count));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return albums;
    }


    /**
     * //根据id获取相册下所有图片
     *
     * @param context
     * @param albumId
     * @return
     */
    public static ArrayList<PictureModel> getPictures(Context context, String albumId) {
        ArrayList<PictureModel> pictures = new ArrayList<>();
        Cursor cursor = null;
        try {
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            if (albumId.equals(ALBUM_ID_ALL_PHOTOS)) {
                //获取sd卡上所有图片
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns,
                        null,
                        null,
                        orderBy);
            } else {
                //根据id获取当前相册下所有图片
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns, "bucket_id = ?", new String[]{albumId}, orderBy);
            }
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
                    PictureModel pic = new PictureModel();
                    pic.setId(path);
                    pic.setUri(Uri.parse("file://" + path));
                    pic.setFile(new File(path));
                    pic.setType(1);
                    pic.setOrientation(orientation);
                    pictures.add(pic);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return pictures;
    }


    /**
     * 获取图片数量
     *
     * @param context
     * @param albumId
     * @return
     */
    public static int getPicturesCount(Context context, String albumId) {
        int count = 0;
        Cursor cursor = null;
        try {
            final String[] columns = {MediaStore.Images.ImageColumns.DATA};
            if (albumId.equals(ALBUM_ID_ALL_PHOTOS)) {
                //获取图片总数
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns, null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED);
                if (cursor != null) {
                    count = cursor.getCount();
                }
            } else {
                //根据id获取相册下的图片数量
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns, "bucket_id = ?", new String[]{albumId},
                        MediaStore.Images.ImageColumns.DATE_MODIFIED);
                if (cursor != null) {
                    count = cursor.getCount();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count;
    }

    /**
     * 取第一张为相册封面
     *
     * @param context
     * @param albumId
     * @return
     */
    public static Uri getAlbumCover(Context context, String albumId) {
        Uri coverUri = null;
        Cursor cursor = null;
        try {
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
            final String[] columns = {MediaStore.Images.Media.DATA};
            if (albumId.equals(ALBUM_ID_ALL_PHOTOS)) {
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns, null, null, orderBy);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        coverUri = Uri.parse("file://" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    }
                }
            } else {
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns,
                        "bucket_id = ?",
                        new String[]{albumId},
                        orderBy);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        coverUri = Uri.parse("file://" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return coverUri;
    }


    private static boolean isAlbumAdded(ArrayList<AlbumModel> albums, String albumId) {
        boolean isAlbumAdded = false;
        for (AlbumModel albumModel : albums) {
            if (albumModel.getAlbumId().equals(albumId)) {
                isAlbumAdded = true;
                break;
            }
        }
        return isAlbumAdded;
    }
}
