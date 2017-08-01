package cn.jack.album.data;

import java.util.ArrayList;

import cn.jack.album.model.PictureModel;

/**
 * Created by Jack on 2017/8/1.
 */

public class PictureData {

    private ArrayList<PictureModel> pictures = new ArrayList<>();


    private PictureData() {

    }


    public static PictureData getInstance() {
        return PictureData.Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private PictureData singleton;

        Singleton() {
            singleton = new PictureData();
        }

        public PictureData getInstance() {
            return singleton;
        }
    }


    public void setData(ArrayList<PictureModel> pictures) {
        this.pictures.clear();
        this.pictures.addAll(pictures);

    }

    public PictureModel getItem(int index) {
        return pictures.get(index);
    }

    public void add(PictureModel picture) {
        pictures.add(picture);
    }

    public void clear() {
        pictures.clear();
    }

    public int getSize() {
        return pictures.size();
    }
}
