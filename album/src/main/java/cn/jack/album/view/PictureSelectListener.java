package cn.jack.album.view;

import java.util.ArrayList;

import cn.jack.album.model.PictureModel;

/**
 * Created by Jack on 2015/10/20.
 */
public interface PictureSelectListener {

    /**
     * @param pictures
     */
    void onPictureSelect(ArrayList<PictureModel> pictures);

}
