package cn.jack.album.data;

import java.util.ArrayList;

import cn.jack.album.config.Config;
import cn.jack.album.model.PictureModel;

/**
 * Created by Jack on 2017/8/1.
 */

public class SelectedPictureData {

    private ArrayList<PictureModel> selectedPictures = new ArrayList<>();


    private SelectedPictureData() {

    }


    public static SelectedPictureData getInstance() {
        return SelectedPictureData.Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private SelectedPictureData singleton;

        Singleton() {
            singleton = new SelectedPictureData();
        }

        public SelectedPictureData getInstance() {
            return singleton;
        }
    }

    public ArrayList<PictureModel> getData() {
        return selectedPictures;
    }

    public void clear() {
        selectedPictures.clear();
    }
    public int getSize() {
        return selectedPictures.size();
    }

    public void add(PictureModel picture) {
        selectedPictures.add(picture);
    }

    public boolean isAdded(PictureModel picture) {
        return isPictureAdded(picture);
    }

    public void remove(PictureModel picture) {
        removePicture(picture);
    }

    public int indexOf(PictureModel picture) {
        return getIndex(picture);
    }

    public boolean canAddMore(){
        return selectedPictures.size() < Config.getInstance().getMaxChoice();
    }

    private boolean isPictureAdded(PictureModel picture) {
        boolean isSelected = false;
        for (PictureModel selectedPicture : selectedPictures) {
            if (selectedPicture.getPath().equals(picture.getPath())) {
                isSelected = true;
                break;
            }
        }
        return isSelected;
    }

    private void removePicture(PictureModel picture) {
        for (int i = 0; i < selectedPictures.size(); i++) {
            if (selectedPictures.get(i).getPath().equals(picture.getPath())) {
                selectedPictures.remove(i);
                break;
            }
        }
    }

    private int getIndex(PictureModel picture) {
        int index = 0;
        for (int i = 0; i < selectedPictures.size(); i++) {
            if (selectedPictures.get(i).getPath().equals(picture.getPath())) {
                index = i;
                break;
            }
        }
        return index;
    }

}
