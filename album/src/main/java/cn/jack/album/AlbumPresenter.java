package cn.jack.album;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jack on 2017/4/6.
 */

public class AlbumPresenter {

    private AlbumActivity view;
    private ProgressDialog progressDialog;
    private ArrayList<AlbumModel> albums = new ArrayList<>();
    private ArrayList<Uri> photos = new ArrayList<>();

    private DiffUtil.DiffResult photoDiffResult;
    private DiffUtil.DiffResult albumDiffResult;

    private boolean enableCamera = false;

    public AlbumPresenter(AlbumActivity view) {
        this.view = view;
        progressDialog = new ProgressDialog(view);
        progressDialog.setMessage("请稍等...");
        progressDialog.setCancelable(false);
    }

    public ArrayList<AlbumModel> getAlbums() {
        return albums;
    }

    public ArrayList<Uri> getPhotos() {
        return photos;
    }

    public void loadAlbumPhotos(String albumId, boolean enableCamera) {
        this.enableCamera = enableCamera;
        new LoadAlbumPhotosTask().execute(albumId);
    }

    public void loadAlbumList() {
        new LoadAlbumListTask().execute();
    }

    private class LoadAlbumPhotosTask extends AsyncTask<String, Void, ArrayList<Uri>> {


        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected ArrayList<Uri> doInBackground(String... albumId) {
            ArrayList<Uri> list = new ArrayList<>();
            if (albumId[0].equals(AlbumMediaScanner.ALBUM_ID_ALL_PHOTOS)) {
                if (enableCamera) {
                    list.add(Uri.parse("http://camera"));
                }
                list.addAll(AlbumMediaScanner.getAllPhotos(view));
            } else {
                list.addAll(AlbumMediaScanner.getPhotosByAlbumId(view, albumId[0]));
            }
            photoDiffResult =
                    DiffUtil.calculateDiff(new AlbumPhotoDiffCallback(photos, list), true);
            return list;
        }


        @Override
        protected void onPostExecute(ArrayList<Uri> result) {
            progressDialog.dismiss();
            view.refreshPhotos(photoDiffResult, result);
        }
    }

    public void scanFile(File scanFile) {
        if (view == null) {
            return;
        }
        AlbumMediaScanner.scanFile(view, scanFile, new AlbumCallback<String>() {
            @Override
            public void onCompleted(String object) {
                if (view != null) {
                    loadAlbumPhotos(AlbumMediaScanner.ALBUM_ID_ALL_PHOTOS, enableCamera);
                }
            }

            @Override
            public void onFailed(String str) {

            }
        });
    }


    private class LoadAlbumListTask extends AsyncTask<Void, Void, ArrayList<AlbumModel>> {

        @Override
        protected ArrayList<AlbumModel> doInBackground(Void... albumId) {
            ArrayList<AlbumModel> list = new ArrayList<>();
            if (view != null) {
                list.addAll(AlbumMediaScanner.getAllAlbums(view));
            }
            albumDiffResult =
                    DiffUtil.calculateDiff(new AlbumDiffCallback(albums, list), true);
            return list;
        }


        @Override
        protected void onPostExecute(ArrayList<AlbumModel> result) {
            if (view != null) {
                view.refreshAlbumList(albumDiffResult, result);
            }
        }
    }

    public void destroy() {
        view = null;
    }
}
