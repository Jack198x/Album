package cn.jack.album.presenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import cn.jack.album.data.AlbumData;
import cn.jack.album.model.AlbumModel;
import cn.jack.album.model.PictureModel;
import cn.jack.album.model.PictureType;
import cn.jack.album.util.AlbumUtil;
import cn.jack.album.util.CameraUtil;
import cn.jack.album.util.Code;
import cn.jack.album.util.CropUtil;
import cn.jack.album.util.FileUtil;
import cn.jack.album.util.MediaScannerUtil;
import cn.jack.album.view.AlbumActivity;

/**
 * Created by Jack on 2017/4/6.
 */

public class AlbumPresenter {

    private AlbumActivity view;
    private ProgressDialog progressDialog;


    public AlbumPresenter(AlbumActivity view) {
        this.view = view;
        progressDialog = new ProgressDialog(view);
        progressDialog.setMessage("请稍等...");
        progressDialog.setCancelable(false);
    }


    public void loadPictures() {
        new LoadPicturesTask().execute(AlbumData.getInstance().getCurrentAlbumId());
    }

    public void loadAlbums() {
        new LoadAlbumsTask().execute();
    }


    private class LoadPicturesTask extends AsyncTask<String, Void, ArrayList<PictureModel>> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected ArrayList<PictureModel> doInBackground(String... albumId) {
            ArrayList<PictureModel> result = AlbumUtil.getPictures(view, albumId[0]);
            if (albumId[0].equals(AlbumUtil.ALBUM_ID_ALL_PHOTOS) && AlbumData.getInstance().isEnableCamera()) {
                PictureModel camera = new PictureModel();
                camera.setType(0);
                result.add(0, camera);
            }
            return result;
        }


        @Override
        protected void onPostExecute(ArrayList<PictureModel> result) {
            AlbumData.getInstance().setPictures(result);
            progressDialog.dismiss();
            view.loadPictures();
        }
    }

    private class LoadAlbumsTask extends AsyncTask<String, Void, ArrayList<AlbumModel>> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected ArrayList<AlbumModel> doInBackground(String... albumId) {
            return AlbumUtil.getAlbums(view);
        }


        @Override
        protected void onPostExecute(ArrayList<AlbumModel> result) {
            AlbumData.getInstance().setAlbums(result);
            progressDialog.dismiss();
            view.loadAlbums();
        }
    }

    public void onPictureClick(PictureModel picture) {
        if (picture.getType() == PictureType.CAMERA) {
            if (AlbumData.getInstance().canSelectMore()) {
                camera();
            } else {
                Toast.makeText(view, "您已经选择了" + AlbumData.getInstance().getMaxChoice() + "张照片了！", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (AlbumData.getInstance().isSingleChoice()) {
                if (AlbumData.getInstance().isEnableCrop()) {
                    crop(picture.getFile());
                } else {
                    AlbumData.getInstance().clearSelectedPictures();
                    AlbumData.getInstance().select(picture);
                }
            } else {
                if (AlbumData.getInstance().isSelected(picture)) {
                    AlbumData.getInstance().deselect(picture);
                } else {
                    if (AlbumData.getInstance().canSelectMore()) {
                        AlbumData.getInstance().select(picture);
                    } else {
                        Toast.makeText(view, "您已经选择了" + AlbumData.getInstance().getMaxChoice() + "张照片了！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void camera() {
        String appendPath = System.currentTimeMillis() + "_camera_pic.jpg";
        final File cameraOutputFile = new File(FileUtil.getSystemPicturePath(), appendPath);
        CameraUtil.camera(view, AlbumData.getInstance().getAuthority(), cameraOutputFile, new CameraUtil.CameraListener() {
            @Override
            public void onCamera(PictureModel cameraPicture) {
                scanFile(cameraPicture.getUri());
                if (AlbumData.getInstance().isSingleChoice()) {
                    if (AlbumData.getInstance().isEnableCrop()) {
                        crop(cameraOutputFile);
                    } else {
                        AlbumData.getInstance().clearSelectedPictures();
                        AlbumData.getInstance().select(cameraPicture);
                        complete();
                    }
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void crop(File cropInputFile) {
        String appendPath = System.currentTimeMillis() + "_crop_pic.jpg";
        final File cropOutPutFile = new File(FileUtil.getSystemPicturePath(), appendPath);
        CropUtil.crop(view, AlbumData.getInstance().getAuthority(), cropInputFile, cropOutPutFile, new CropUtil.CropListener() {
            @Override
            public void onCrop(PictureModel cropPicture) {
                AlbumData.getInstance().clearSelectedPictures();
                AlbumData.getInstance().select(cropPicture);
                complete();
            }
        });
    }


    private void scanFile(Uri uri) {
        if (view == null) {
            return;
        }
        MediaScannerUtil.scanFile(view, uri, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                if (view != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            loadPictures();
                        }
                    });
                }
            }
        });
    }


    public void complete() {
        if (AlbumData.getInstance().getSelectedPictures().size() > 0) {
            Intent intent = new Intent();
            view.setResult(Code.RESULT_OK, intent);
        }
        view.finish();
    }

    public void destroy() {
        view = null;
    }
}
