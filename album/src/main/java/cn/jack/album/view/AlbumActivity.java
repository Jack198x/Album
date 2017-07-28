package cn.jack.album.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import cn.jack.album.R;
import cn.jack.album.data.AlbumData;
import cn.jack.album.model.AlbumModel;
import cn.jack.album.model.PictureModel;
import cn.jack.album.presenter.AlbumPresenter;
import cn.jack.album.util.Code;


public class AlbumActivity extends AppCompatActivity {


    private static final int SPAN_COUNT = 3;


    private RecyclerView pictureRecyclerView;
    private RecyclerView albumRecyclerView;
    private Button albumListButton;


    private AlbumAdapter albumAdapter;
    private PictureAdapter pictureAdapter;

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
        pictureRecyclerView = (RecyclerView) findViewById(R.id.pictureRecyclerView);
        albumRecyclerView = (RecyclerView) findViewById(R.id.albumRecyclerView);
        albumListButton = (Button) findViewById(R.id.albumListButton);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        albumRecyclerView.setLayoutManager(linearLayoutManager);
        pictureRecyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        int spacing = getResources().getDimensionPixelSize(R.dimen.media_grid_spacing);
        pictureRecyclerView.addItemDecoration(new GridItemDecoration(SPAN_COUNT, spacing, false));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(AlbumData.getInstance().getTitle());
        }

        albumAdapter = new AlbumAdapter(this, onAlbumClickListener);
        albumRecyclerView.setAdapter(albumAdapter);

        pictureAdapter = new PictureAdapter(this, onPictureClickListener);
        pictureRecyclerView.setAdapter(pictureAdapter);
    }


    protected void bind() {
        presenter = new AlbumPresenter(this);
        presenter.loadAlbums();
        presenter.loadPictures();
    }

    protected void setListeners() {
        albumListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AlbumData.getInstance().getAlbums().size() > 0) {
                    if (albumRecyclerView.getVisibility() == View.VISIBLE) {
                        albumRecyclerView.setVisibility(View.GONE);
                    } else {
                        albumRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
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
        if (!AlbumData.getInstance().isSingleChoice()) {
            getMenuInflater().inflate(R.menu.menu_album_confirm, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_confirm) {
            presenter.complete();
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadPictures() {
        pictureAdapter.notifyDataSetChanged();
    }

    public void loadAlbums() {
        albumAdapter.notifyDataSetChanged();
    }


    private AlbumAdapter.OnItemClickListener onAlbumClickListener = new AlbumAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(AlbumModel album) {
            albumRecyclerView.setVisibility(View.GONE);
            Log.e("getAlbumId", album.getAlbumId() + "");
            AlbumData.getInstance().setCurrentAlbumId(album.getAlbumId());
            presenter.loadPictures();
        }
    };


    private PictureAdapter.OnItemClickListener onPictureClickListener = new PictureAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(PictureModel data) {
            presenter.onPictureClick(data);
            pictureAdapter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (albumRecyclerView.getVisibility() == View.VISIBLE) {
            albumRecyclerView.setVisibility(View.GONE);
        } else {
            setResult(Code.RESULT_CANCEL);
            super.onBackPressed();
        }
    }

}
