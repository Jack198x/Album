package cn.jack.albumexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import cn.jack.album.model.PictureModel;
import cn.jack.album.util.CameraUtil;
import cn.jack.album.util.FileUtil;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private File cameraOutPutFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraOutPutFile = new File(FileUtil.getSystemPicturePath(), System.currentTimeMillis() + "_" + "test.jpg");
                CameraUtil.camera(MainActivity.this, getString(R.string.album), cameraOutPutFile, new CameraUtil.CameraListener() {
                    @Override
                    public void onCamera(PictureModel cameraPicture) {
                        imageView.setImageURI(cameraPicture.getUri());
                    }
                });
//                Album.with(MainActivity.this, getString(R.string.album))
//                        .title("Album")
//                        .enableCrop(true)
//                        .enableCamera(false)
//                        .maxChoice(2)
//                        .setListener(new PictureSelectListener() {
//                            @Override
//                            public void onPictureSelect(ArrayList<PictureModel> pictures) {
//                                imageView.setImageURI(pictures.get(0).getUri());
//                            }
//                        })
//                        .open();

            }
        });
    }
}
