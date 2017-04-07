package cn.jack.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.jack.album.util.AlbumImageLoader;


/**
 * Created by Jack on 2015/10/20.
 */
public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ListViewHolder> {


    private Context context;
    private ArrayList<AlbumModel> albumList;
    private OnItemClickListener onItemClickListener;

    public AlbumListAdapter(Context context, ArrayList<AlbumModel> albumList, OnItemClickListener onItemClickListener) {
        super();
        this.context = context;
        this.albumList = albumList;
        this.onItemClickListener = onItemClickListener;

    }


    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_album_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        final AlbumModel albumModel = albumList.get(position);
        AlbumImageLoader.with(context, albumModel.getCoverUri())
                .centerCrop()
                .resize(400, 400)
                .into(holder.albumListItemCoverImageView);
        holder.albumListItemNameTextView.setText(albumModel.getAlbumName());
        holder.albumListItemCountTextView.setText(albumModel.getPhotoCount() + "张");
        holder.albumListItemRootLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(albumModel.getAlbumId(), albumModel.getAlbumName(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout albumListItemRootLinearLayout;
        public ImageView albumListItemCoverImageView;
        public TextView albumListItemNameTextView;
        public TextView albumListItemCountTextView;

        public ListViewHolder(View view) {
            super(view);
            this.albumListItemRootLinearLayout = (LinearLayout) view.findViewById(R.id.albumListItemRootLinearLayout);
            this.albumListItemCoverImageView = (ImageView) view.findViewById(R.id.albumListItemCoverImageView);
            this.albumListItemNameTextView = (TextView) view.findViewById(R.id.albumListItemNameTextView);
            this.albumListItemCountTextView = (TextView) view.findViewById(R.id.albumListItemCountTextView);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(String albumId, String albumName, int position);
    }
}
