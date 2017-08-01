package cn.jack.album.view;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jack.album.R;
import cn.jack.album.data.AlbumData;
import cn.jack.album.model.AlbumModel;
import cn.jack.glideimageview.GlideImageView;

/**
 * Created by Jack on 2017/7/28.
 */

public class AlbumAdapter extends RecyclerView.Adapter<BaseViewHolder<AlbumModel>> {

    private Activity activity;
    private OnItemClickListener listener = null;

    public AlbumAdapter(Activity activity) {
        this.activity = activity;
    }

    public AlbumAdapter(Activity activity, OnItemClickListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public BaseViewHolder<AlbumModel> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlbumHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_list, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<AlbumModel> holder, int position) {
        holder.parse(activity, AlbumData.getInstance().getItem(position));
    }

    @Override
    public int getItemCount() {
        return AlbumData.getInstance().getSize();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private class AlbumHolder extends BaseViewHolder<AlbumModel> {
        private LinearLayout albumListItemRootLinearLayout;
        public GlideImageView albumListItemCoverImageView;
        public TextView albumListItemNameTextView;
        public TextView albumListItemCountTextView;

        public AlbumHolder(View view) {
            super(view);
            this.albumListItemRootLinearLayout = (LinearLayout) view.findViewById(R.id.albumListItemRootLinearLayout);
            this.albumListItemCoverImageView = (GlideImageView) view.findViewById(R.id.albumListItemCoverImageView);
            this.albumListItemNameTextView = (TextView) view.findViewById(R.id.albumListItemNameTextView);
            this.albumListItemCountTextView = (TextView) view.findViewById(R.id.albumListItemCountTextView);
        }

        @Override
        public void parse(Activity activity, final AlbumModel album) {
            albumListItemCoverImageView.centerCrop();
            albumListItemCoverImageView.setImageUri(album.getCoverUri());
            albumListItemNameTextView.setText(album.getAlbumName());
            albumListItemCountTextView.setText(album.getPhotoCount() + "张");
            albumListItemRootLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(album);
                    }
                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(AlbumModel album);
    }
}
