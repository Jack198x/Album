package cn.jack.albumexample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import cn.jack.album.Album;
import cn.jack.album.AlbumListener;
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
                Album.with(MainActivity.this, getString(R.string.album))
                        .title("Album")
                        .enableCrop(false)
                        .enableCamera(true)
                        .maxChoice(10)
                        .setListener(new AlbumListener() {
                            @Override
                            public void onPhotosSelected(ArrayList<String> photos) {
                                imageView.setImageURI(Uri.parse(photos.get(0)));
                            }

                            @Override
                            public void onError(String error) {

                            }
                        })
                        .open();

            }
        });
    }
}
