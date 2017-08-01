package cn.jack.album.data;

import java.util.ArrayList;

import cn.jack.album.model.AlbumModel;
import cn.jack.album.util.AlbumLoader;

/**
 * Created by Jack on 2017/7/24.
 */

public class AlbumData {

    private ArrayList<AlbumModel> albums = new ArrayList<>();
    private String currentAlbumId = AlbumLoader.ALBUM_ID_ALL_IMAGES;

    private AlbumData() {

    }


    public static AlbumData getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private AlbumData singleton;

        Singleton() {
            singleton = new AlbumData();
        }

        public AlbumData getInstance() {
            return singleton;
        }
    }


    public String getCurrentAlbumId() {
        return currentAlbumId;
    }

    public void setCurrentAlbumId(String currentAlbumId) {
        this.currentAlbumId = currentAlbumId;
    }


    public int getSize() {
        return albums.size();
    }

    public void clear() {
        albums.clear();
    }

    public AlbumModel getItem(int index) {
        return albums.get(index);
    }

    public void add(AlbumModel album) {
        int index = indexOf(album.getAlbumId());
        if (index >= 0) {
            albums.get(index).setPhotoCount(albums.get(index).getPhotoCount() + 1);
        } else {
            albums.add(album);
        }
    }


    private int indexOf(String albumId) {
        int index = -1;
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getAlbumId().equals(albumId)) {
                index = i;
                break;
            }
        }
        return index;
    }


}
