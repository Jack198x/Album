package cn.jack.album;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import cn.jack.album.util.FileUtil;
import cn.jack.album.util.PermissionUtil;


public class AlbumActivity extends AppCompatActivity {

    private static final int TYPE_SINGLE_CHOOSE = 1;

    private static final int SPAN_COUNT = 3;
    private static final String CAMERA_FILE_NAME = "camera_pic.jpg";
    private static final String CROP_FILE_NAME = "crop_pic.jpg";

    private RecyclerView albumGridRecyclerView;
    private RecyclerView albumListRecyclerView;
    private Button albumListButton;

    private boolean enableCamera = false;
    private boolean enableCrop = false;
    private int maxChoice = 1;

    private String currentAlbumId = AlbumMediaScanner.ALBUM_ID_ALL_PHOTOS;

    private File cameraOutputFile = null;
    private Uri cropOutPutUri = null;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private AlbumGridAdapter albumGridAdapter;
    private AlbumListAdapter albumListAdapter;


    private AlbumPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        bind();
        setListeners();
    }

    protected void initViews() {
        setContentView(R.layout.activity_album);
        initToolbar();
        Intent intent = getIntent();
        enableCamera = intent.getBooleanExtra("enableCamera", false);
        enableCrop = intent.getBooleanExtra("enableCrop", false);
        maxChoice = intent.getIntExtra("maxLimit", TYPE_SINGLE_CHOOSE);
        albumGridRecyclerView = (RecyclerView) findViewById(R.id.albumGridRecyclerView);
        albumListRecyclerView = (RecyclerView) findViewById(R.id.albumListRecyclerView);
        albumListButton = (Button) findViewById(R.id.albumListButton);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        albumListRecyclerView.setLayoutManager(linearLayoutManager);
        albumGridRecyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        albumGridRecyclerView.addItemDecoration(new AlbumSpacesItemDecoration(2));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));
            if (maxChoice > 1) {
                getSupportActionBar().setSubtitle("0/" + maxChoice);
            }
        }
    }


    protected void bind() {
        presenter = new AlbumPresenter(this);
        albumGridAdapter = new AlbumGridAdapter(AlbumActivity.this, presenter.getPhotos(), enableCamera, maxChoice);
        albumListAdapter = new AlbumListAdapter(AlbumActivity.this, presenter.getAlbums(), listClickListener);
        albumGridRecyclerView.setAdapter(albumGridAdapter);
        albumListRecyclerView.setAdapter(albumListAdapter);
        presenter.loadAlbumPhotos(AlbumMediaScanner.ALBUM_ID_ALL_PHOTOS);
        presenter.loadAlbumList();
    }

    protected void setListeners() {
        albumListButton.setOnClickListener(listener);
        albumGridAdapter.setOnItemClickListener(gridClickListener);
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (maxChoice > 1) {
            getMenuInflater().inflate(R.menu.menu_album_confirm, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_confirm) {
            if (selectedPhotos.size() > 0) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("selectedPhotos", selectedPhotos);
                setResult(Album.RESULT_OK, intent);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.albumListButton) {
                if (presenter.getAlbums().size() > 0) {
                    if (albumListRecyclerView.getVisibility() == View.VISIBLE) {
                        albumListRecyclerView.setVisibility(View.GONE);
                    } else {
                        albumListRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    };


    public void refreshPhotos(DiffUtil.DiffResult photoDiffResult, ArrayList<Uri> data) {
        photoDiffResult.dispatchUpdatesTo(albumGridAdapter);
        presenter.getPhotos().clear();
        presenter.getPhotos().addAll(data);
    }

    public void refreshAlbumList(DiffUtil.DiffResult albumDiffResult, ArrayList<AlbumModel> data) {
        albumDiffResult.dispatchUpdatesTo(albumListAdapter);
        presenter.getAlbums().clear();
        presenter.getAlbums().addAll(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Album.REQUEST_CODE_CAMERA) {
                //多选进入(不支持裁剪)，点击拍照，拍照结束直接返回
                if (maxChoice > 1) {
                    if (selectedPhotos.size() < maxChoice) {
                        selectedPhotos.add(cameraOutputFile.getPath());
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("selectedPhotos", selectedPhotos);
                        setResult(Album.RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(AlbumActivity.this, "您已选择" + maxChoice + "张照片了！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    scanFile(cameraOutputFile);
                    if (enableCrop) {
                        cropOutPutUri = Uri
                                .fromFile(FileUtil.getSystemPicturePath())
                                .buildUpon()
                                .appendPath(System.currentTimeMillis() + "_" + CROP_FILE_NAME)
                                .build();
                        Album.with(AlbumActivity.this).openCrop(cameraOutputFile, cropOutPutUri);
                    }
                }
            }
            if (requestCode == Album.REQUEST_CODE_CROP) {
                if (maxChoice == TYPE_SINGLE_CHOOSE) {
                    selectedPhotos.clear();
                    selectedPhotos.add(cropOutPutUri.getPath());
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("selectedPhotos", selectedPhotos);
                    setResult(Album.RESULT_OK, intent);
                    finish();
                } else {
                    if (selectedPhotos.size() < maxChoice) {
                        selectedPhotos.add(cropOutPutUri.getPath());
                    } else {
                        Toast.makeText(AlbumActivity.this, "您已选择" + maxChoice + "张照片了！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    private void scanFile(File scanFile) {
        AlbumMediaScanner.scanFile(AlbumActivity.this, scanFile, new AlbumCallback<String>() {
            @Override
            public void onCompleted(String object) {
                presenter.loadAlbumPhotos(AlbumMediaScanner.ALBUM_ID_ALL_PHOTOS);
            }

            @Override
            public void onFailed(String str) {

            }
        });
    }


    private AlbumGridAdapter.OnItemClickListener gridClickListener = new AlbumGridAdapter.OnItemClickListener() {

        @Override
        public void onCameraClick() {
            if (PermissionUtil.checkPermission(AlbumActivity.this, Manifest.permission.CAMERA)) {
                cameraOutputFile = new File(FileUtil.getSystemPicturePath(), System.currentTimeMillis() + "_" + CAMERA_FILE_NAME);
                Album.with(AlbumActivity.this).openCamera(cameraOutputFile);
            } else {
                PermissionUtil.requestPermission(AlbumActivity.this, Manifest.permission.CAMERA);
                Toast.makeText(AlbumActivity.this, R.string.no_camera_permission, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onItemClick(Uri uri) {
            if (maxChoice == TYPE_SINGLE_CHOOSE) {
                if (enableCrop) {
                    cropOutPutUri = Uri
                            .fromFile(FileUtil.getSystemPicturePath())
                            .buildUpon()
                            .appendPath(System.currentTimeMillis() + "_" + CROP_FILE_NAME)
                            .build();
                    Album.with(AlbumActivity.this).openCrop(uri, cropOutPutUri);
                } else {
                    selectedPhotos.clear();
                    selectedPhotos.add(uri.getPath());
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("selectedPhotos", selectedPhotos);
                    setResult(Album.RESULT_OK, intent);
                    finish();
                }

            } else {
                if (enableCrop) {
                    cropOutPutUri = Uri
                            .fromFile(FileUtil.getSystemPicturePath())
                            .buildUpon()
                            .appendPath(System.currentTimeMillis() + "_" + CROP_FILE_NAME)
                            .build();
                    Album.with(AlbumActivity.this).openCrop(uri, cropOutPutUri);
                } else {
                    if (selectedPhotos.size() < maxChoice) {
                        selectedPhotos.add(uri.getPath());
                    } else {
                        Toast.makeText(AlbumActivity.this, "您已选择" + maxChoice + "张照片了！", Toast.LENGTH_SHORT).show();
                    }
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setSubtitle(selectedPhotos.size() + "/" + maxChoice);
                    }

                }
            }
        }

        @Override
        public void onItemCancelClick(Uri uri) {
            if (selectedPhotos.size() > 0 && selectedPhotos.contains(uri.getPath())) {
                selectedPhotos.remove(uri.getPath());
            }
            if (getSupportActionBar() != null) {
                getSupportActionBar().setSubtitle(selectedPhotos.size() + "/" + maxChoice);
            }
        }
    };


    private AlbumListAdapter.OnItemClickListener listClickListener = new AlbumListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(String albumId, String albumName, int position) {
            if (!albumId.equals(currentAlbumId)) {
                currentAlbumId = albumId;
                albumListButton.setText(albumName);
                presenter.loadAlbumPhotos(currentAlbumId);
            }
            albumListRecyclerView.setVisibility(View.GONE);
        }
    };


    @Override
    public void onBackPressed() {
        if (albumListRecyclerView.getVisibility() == View.VISIBLE) {
            albumListRecyclerView.setVisibility(View.GONE);
        } else {
            setResult(Album.RESULT_CANCEL);
            super.onBackPressed();
        }
    }

}
