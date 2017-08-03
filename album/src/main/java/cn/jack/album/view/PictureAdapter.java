package cn.jack.album.view;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import cn.jack.album.R;
import cn.jack.album.config.Config;
import cn.jack.album.data.AlbumData;
import cn.jack.album.data.PictureData;
import cn.jack.album.data.SelectedPictureData;
import cn.jack.album.model.PictureModel;
import cn.jack.album.util.AlbumLoader;
import cn.jack.glideimageview.GlideImageView;

/**
 * Created by Jack on 2017/7/27.
 */

public class PictureAdapter extends RecyclerView.Adapter<BaseViewHolder<PictureModel>> {

    private static final int TYPE_CAMERA = 0x0001;
    private static final int TYPE_PICTURE = 0x0002;


    private Activity activity;
    private OnItemClickListener listener = null;

    public PictureAdapter(Activity activity) {
        this.activity = activity;
    }

    public PictureAdapter(Activity activity, OnItemClickListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (isCamera(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    @Override
    public BaseViewHolder<PictureModel> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            return new CameraHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_grid_camera, parent, false));
        } else {
            return new PictureHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_grid, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<PictureModel> holder, int position) {
        if (enableCamera()) {
            if (position == 0) {
                holder.parse(activity, null);
            } else {
                holder.parse(activity, PictureData.getInstance().getItem(position - 1));
            }
        } else {
            holder.parse(activity, PictureData.getInstance().getItem(position));
        }

    }

    @Override
    public int getItemCount() {
        if (enableCamera()) {
            return PictureData.getInstance().getSize() + 1;
        } else {
            return PictureData.getInstance().getSize();
        }
    }


    private class PictureHolder extends BaseViewHolder<PictureModel> {
        GlideImageView albumGridItemImageView;
        ImageView gifTagImageView;
        CheckView albumGridItemCheckView;
        View disableCoverView;
        Button albumGridItemButton;

        PictureHolder(View view) {
            super(view);
            this.albumGridItemImageView = (GlideImageView) view.findViewById(R.id.albumGridItemImageView);
            this.gifTagImageView = (ImageView) view.findViewById(R.id.gifTagImageView);
            this.albumGridItemCheckView = (CheckView) view.findViewById(R.id.albumGridItemCheckView);
            this.disableCoverView = view.findViewById(R.id.disableCoverView);
            this.albumGridItemButton = (Button) view.findViewById(R.id.albumGridItemButton);
        }

        @Override
        public void parse(final Activity activity, final PictureModel picture) {
            albumGridItemImageView.centerCrop();
            albumGridItemImageView.setImageUri(picture.getUri());
            gifTagImageView.setVisibility(picture.isGif() ? View.VISIBLE : View.INVISIBLE);
            if (Config.getInstance().isSingleChoice()) {
                disableCoverView.setVisibility(View.INVISIBLE);
                albumGridItemCheckView.setVisibility(View.GONE);
            } else {
                albumGridItemCheckView.setVisibility(View.VISIBLE);
                albumGridItemCheckView.setCountable(true);
                if (SelectedPictureData.getInstance().isAdded(picture)) {
                    albumGridItemCheckView.setEnabled(true);
                    albumGridItemCheckView.setCheckedNum(SelectedPictureData.getInstance().indexOf(picture) + 1);
                    disableCoverView.setVisibility(View.INVISIBLE);
                } else {
                    albumGridItemCheckView.setCheckedNum(CheckView.UNCHECKED);
                    albumGridItemCheckView.setEnabled(SelectedPictureData.getInstance().canAddMore());
                    disableCoverView.setVisibility(SelectedPictureData.getInstance().canAddMore() ? View.INVISIBLE : View.VISIBLE);
                }
            }
            albumGridItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onPictureClick(picture);
                    }
                }
            });

        }
    }

    private class CameraHolder extends BaseViewHolder<PictureModel> {
        Button albumGridItemCameraButton;

        CameraHolder(View view) {
            super(view);
            this.albumGridItemCameraButton = (Button) view.findViewById(R.id.albumGridItemCameraButton);
        }

        @Override
        public void parse(final Activity activity, final PictureModel data) {
            albumGridItemCameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCameraClick();
                    }
                }
            });
        }
    }

    private boolean isCamera(int position) {
        return enableCamera() && position == 0;
    }

    private boolean enableCamera(){
        return Config.getInstance().isEnableCamera() && AlbumData.getInstance().getCurrentAlbumId().equals(AlbumLoader.ALBUM_ID_ALL_IMAGES);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    interface OnItemClickListener {

        void onCameraClick();

        void onPictureClick(PictureModel data);

    }


}
