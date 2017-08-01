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

import cn.jack.album.config.Config;
import cn.jack.album.data.AlbumData;
import cn.jack.album.data.SelectedPictureData;
import cn.jack.album.model.PictureModel;
import cn.jack.album.util.AlbumLoader;
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


    public void load() {
        new LoadPicturesTask().execute(AlbumData.getInstance().getCurrentAlbumId());
    }


    private class LoadPicturesTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... albumId) {
            new AlbumLoader(view).load(albumId[0]);
            return albumId[0];
        }

        @Override
        protected void onPostExecute(String albumId) {
            super.onPostExecute(albumId);
            progressDialog.dismiss();
            view.refreshAlbum();
        }
    }

    public void onCameraClick(){
        if (SelectedPictureData.getInstance().canAddMore()) {
            camera();
        } else {
            Toast.makeText(view, "您已经选择了" + Config.getInstance().getMaxChoice() + "张照片了！", Toast.LENGTH_SHORT).show();
        }
    }

        public void onPictureClick(PictureModel picture) {
            if (Config.getInstance().isSingleChoice()) {
                if (Config.getInstance().isEnableCrop()) {
                    crop(picture.getFile());
                } else {
                    SelectedPictureData.getInstance().clear();
                    SelectedPictureData.getInstance().add(picture);
                }
            } else {
                if (SelectedPictureData.getInstance().isAdded(picture)) {
                    SelectedPictureData.getInstance().remove(picture);
                } else {
                    if (SelectedPictureData.getInstance().canAddMore()) {
                        SelectedPictureData.getInstance().add(picture);
                    } else {
                        Toast.makeText(view, "您已经选择了" + Config.getInstance().getMaxChoice() + "张照片了！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        private void camera() {
            String appendPath = System.currentTimeMillis() + "_camera_pic.jpg";
            final File cameraOutputFile = new File(FileUtil.getSystemPicturePath(), appendPath);
            CameraUtil.camera(view, Config.getInstance().getAuthority(), cameraOutputFile, new CameraUtil.CameraListener() {
                @Override
                public void onCamera(PictureModel cameraPicture) {
                    scanFile(cameraPicture.getUri());
                    if (Config.getInstance().isSingleChoice()) {
                        if (Config.getInstance().isEnableCrop()) {
                            crop(cameraOutputFile);
                        } else {
                            SelectedPictureData.getInstance().clear();
                            SelectedPictureData.getInstance().add(cameraPicture);
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
            CropUtil.crop(view, Config.getInstance().getAuthority(), cropInputFile, cropOutPutFile, new CropUtil.CropListener() {
                @Override
                public void onCrop(PictureModel cropPicture) {
                    SelectedPictureData.getInstance().clear();
                    SelectedPictureData.getInstance().add(cropPicture);
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
                                load();
                            }
                        });
                    }
                }
            });
        }


        public void complete() {
            if (SelectedPictureData.getInstance().getSize() > 0) {
                Intent intent = new Intent();
                view.setResult(Code.RESULT_OK, intent);
            }
            view.finish();
        }

        public void destroy() {
            view = null;
        }
}
