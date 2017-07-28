package cn.jack.album;

import java.util.ArrayList;

/**
 * Created by Jack on 2015/10/20.
 */
public interface PictureSelectListener {

    /**
     * @param pictures
     */
    void onPictureSelect(ArrayList<PictureModel> pictures);

}
