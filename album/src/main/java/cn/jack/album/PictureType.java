package cn.jack.album;

/**
 * Created by Jack on 2017/7/27.
 */

public enum PictureType {

    CAMERA(0),
    PICTURE(1);

    private int code;

    public int getCode() {
        return code;
    }

    public static PictureType fromInt(int code) {
        switch (code) {
            case 0:
                return CAMERA;
            case 1:
                return PICTURE;
            default:
                return PICTURE;
        }
    }


    PictureType(int i) {
        this.code = i;
    }

}
