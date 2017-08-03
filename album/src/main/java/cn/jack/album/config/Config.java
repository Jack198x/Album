package cn.jack.album.config;

import cn.jack.album.data.AlbumData;
import cn.jack.album.data.PictureData;
import cn.jack.album.data.SelectedPictureData;
import cn.jack.album.util.AlbumLoader;

/**
 * Created by Jack on 2017/8/1.
 */

public class Config {

    private String title = "选择图片";
    private String authority;
    private boolean enableCamera = false;
    private boolean enableCrop = false;
    private int maxChoice = 1;


    private Config() {

    }

    public static Config getInstance() {
        return Config.Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private Config singleton;

        Singleton() {
            singleton = new Config();
        }

        public Config getInstance() {
            return singleton;
        }
    }


    public void init(String authority) {
        title = "选择图片";
        this.authority = authority;
        enableCamera = false;
        enableCrop = false;
        maxChoice = 1;
        AlbumData.getInstance().clear();
        AlbumData.getInstance().setCurrentAlbumId(AlbumLoader.ALBUM_ID_ALL_IMAGES);
        PictureData.getInstance().clear();
        SelectedPictureData.getInstance().clear();
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

    public boolean isEnableCamera() {
        return enableCamera;
    }

    public void setEnableCamera(boolean enableCamera) {
        this.enableCamera = enableCamera;
    }

    public boolean isEnableCrop() {
        return enableCrop;
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

    public boolean isSingleChoice(){
        return maxChoice==1;
    }
}
