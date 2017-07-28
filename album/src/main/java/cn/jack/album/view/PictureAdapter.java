package cn.jack.album.view;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import cn.jack.album.BaseViewHolder;
import cn.jack.album.CheckView;
import cn.jack.album.PictureModel;
import cn.jack.album.PictureType;
import cn.jack.album.R;
import cn.jack.album.data.AlbumData;
import cn.jack.glideimageview.GlideImageView;

/**
 * Created by Jack on 2017/7/27.
 */

public class PictureAdapter extends RecyclerView.Adapter<BaseViewHolder<PictureModel>> {


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
        return AlbumData.getInstance().getPictures().get(position).getType().getCode();
    }

    @Override
    public BaseViewHolder<PictureModel> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (PictureType.fromInt(viewType) == PictureType.CAMERA) {
            return new CameraHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_grid_camera, parent, false));
        } else {
            return new PictureHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_grid, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<PictureModel> holder, int position) {
        holder.parse(activity, AlbumData.getInstance().getPictures().get(position));
    }

    @Override
    public int getItemCount() {
        return AlbumData.getInstance().getPictures().size();
    }


    private class PictureHolder extends BaseViewHolder<PictureModel> {
        GlideImageView albumGridItemImageView;
        CheckView albumGridItemCheckView;
        View disableCoverView;
        Button albumGridItemButton;

        PictureHolder(View view) {
            super(view);
            this.albumGridItemImageView = (GlideImageView) view.findViewById(R.id.albumGridItemImageView);
            this.albumGridItemCheckView = (CheckView) view.findViewById(R.id.albumGridItemCheckView);
            this.disableCoverView = view.findViewById(R.id.disableCoverView);
            this.albumGridItemButton = (Button) view.findViewById(R.id.albumGridItemButton);
        }

        @Override
        public void parse(final Activity activity, final PictureModel picture) {
            albumGridItemImageView.centerCrop();
            albumGridItemImageView.setImageUri(picture.getUri());
            if (AlbumData.getInstance().isSingleChoice()) {
                disableCoverView.setVisibility(View.INVISIBLE);
                albumGridItemCheckView.setVisibility(View.GONE);
            } else {
                albumGridItemCheckView.setVisibility(View.VISIBLE);
                albumGridItemCheckView.setCountable(true);
                if (AlbumData.getInstance().isSelected(picture)) {
                    albumGridItemCheckView.setEnabled(true);
                    albumGridItemCheckView.setCheckedNum(AlbumData.getInstance().indexOf(picture) + 1);
                    disableCoverView.setVisibility(View.INVISIBLE);
                } else {
                    albumGridItemCheckView.setCheckedNum(CheckView.UNCHECKED);
                    albumGridItemCheckView.setEnabled(AlbumData.getInstance().canSelectMore());
                    disableCoverView.setVisibility(AlbumData.getInstance().canSelectMore() ? View.INVISIBLE : View.VISIBLE);
                }
            }
            albumGridItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(picture);
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
                        listener.onItemClick(data);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {

        void onItemClick(PictureModel data);

    }


}
