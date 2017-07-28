package cn.jack.album.data;

import java.util.ArrayList;

import cn.jack.album.AlbumModel;
import cn.jack.album.AlbumUtil;
import cn.jack.album.PictureModel;

/**
 * Created by Jack on 2017/7/24.
 */

public class AlbumData {


    private ArrayList<AlbumModel> albums = new ArrayList<>();
    private ArrayList<PictureModel> pictures = new ArrayList<>();
    private ArrayList<PictureModel> selectedPictures = new ArrayList<>();

    private String title = "选择图片";
    private String authority;
    private boolean enableCamera = false;
    private boolean enableCrop = false;
    private int maxChoice = 1;

    private String currentAlbumId = AlbumUtil.ALBUM_ID_ALL_PHOTOS;


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

    public void init(String authority) {
        title = "选择照片";
        this.authority = authority;
        enableCamera = false;
        enableCrop = false;
        maxChoice = 1;
        albums.clear();
        pictures.clear();
        selectedPictures.clear();
    }

    public String getCurrentAlbumId() {
        return currentAlbumId;
    }

    public void setCurrentAlbumId(String currentAlbumId) {
        this.currentAlbumId = currentAlbumId;
    }

    public ArrayList<AlbumModel> getAlbums() {
        return albums;
    }

    public ArrayList<PictureModel> getPictures() {
        return pictures;
    }

    public ArrayList<PictureModel> getSelectedPictures() {
        return selectedPictures;
    }

    public void clearSelectedPictures() {
        selectedPictures.clear();
    }

    public void select(PictureModel picture) {
        selectedPictures.add(picture);
    }

    public boolean isSelected(PictureModel picture) {
        return isPictureSelected(picture);
    }

    public boolean deselect(PictureModel picture) {
        return selectedPictures.remove(picture);
    }

    public int indexOf(PictureModel picture) {
        return getIndex(picture);
    }

    public void setAlbums(ArrayList<AlbumModel> albums) {
        this.albums.clear();
        this.albums.addAll(albums);
    }

    public void setPictures(ArrayList<PictureModel> pictures) {
        this.pictures.clear();
        this.pictures.addAll(pictures);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public boolean isEnableCamera() {
        return enableCamera;
    }

    public void setEnableCamera(boolean enableCamera) {
        this.enableCamera = enableCamera;
    }

    public boolean isEnableCrop() {
        return maxChoice == 1 && enableCrop;
    }

    public void setEnableCrop(boolean enableCrop) {
        this.enableCrop = enableCrop;
    }

    public int getMaxChoice() {
        return maxChoice;
    }

    public void setMaxChoice(int maxChoice) {
        this.maxChoice = maxChoice;
    }

    public boolean canSelectMore() {
        return selectedPictures.size() < maxChoice;
    }

    public boolean isSingleChoice() {
        return maxChoice == 1;
    }

    private int getIndex(PictureModel picture) {
        int index = 0;
        for (int i = 0; i < selectedPictures.size(); i++) {
            if (selectedPictures.get(i).getId().equals(picture.getId())) {
                index = i;
                break;
            }
        }
        return index;
    }

    private boolean isPictureSelected(PictureModel picture) {
        boolean isSelected = false;
        for (PictureModel selectedPicture : selectedPictures) {
            if (selectedPicture.getId().equals(picture.getId())) {
                isSelected = true;
                break;
            }
        }
        return isSelected;
    }
}
