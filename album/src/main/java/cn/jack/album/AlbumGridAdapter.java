package cn.jack.album;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.jack.glideimageview.GlideImageView;


/**
 * Created by Jack on 2015/10/20.
 */
public class AlbumGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SINGLE_CHOOSE = 1;
    private static final String fileCamera = "camera";

    private static final int TYPE_CAMERA = 0x0001;
    private static final int TYPE_ITEM = 0x0002;

    private ArrayList<Uri> uris;
    private Activity activity;
    private OnItemClickListener listener = null;
    private int maxChoose;
    private List<Uri> uriSet = new ArrayList<>();


    public AlbumGridAdapter(Activity activity, ArrayList<Uri> uris, int maxChoose) {
        super();
        this.maxChoose = maxChoose;
        this.activity = activity;
        this.uris = uris;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && uris.get(0).getHost().equals(fileCamera)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == TYPE_CAMERA ?
                new CameraViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_album_grid_camera, parent, false))
                : new GridViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_album_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int itemType = getItemViewType(position);
        if (itemType == TYPE_CAMERA) {
            CameraViewHolder cameraViewHolder = (CameraViewHolder) holder;
            cameraViewHolder.albumGridItemCameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (maxChoose == TYPE_SINGLE_CHOOSE) { //单选照片
                            listener.onCameraClick();
                        } else {  // 多选照片
                            if (uriSet.size() < maxChoose) {
                                listener.onCameraClick();
                            } else {
                                Toast.makeText(activity, "您已经选择了" + maxChoose + "张照片了！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        } else {
            final GridViewHolder gridViewHolder = (GridViewHolder) holder;
            final Uri uri = uris.get(position);
            gridViewHolder.albumGridItemImageView.centerCrop();
            gridViewHolder.albumGridItemImageView.setImageUri(uri);
            if (maxChoose > TYPE_SINGLE_CHOOSE) {
                gridViewHolder.albumGridItemCheckView.setVisibility(View.VISIBLE);
                gridViewHolder.albumGridItemCheckView.setCountable(true);
                if (uriSet.contains(uri)) {
                    gridViewHolder.albumGridItemCheckView.setEnabled(true);
                    gridViewHolder.albumGridItemCheckView.setCheckedNum(uriSet.indexOf(uri) + 1);
                } else {
                    gridViewHolder.albumGridItemCheckView.setCheckedNum(CheckView.UNCHECKED);
                    gridViewHolder.albumGridItemCheckView.setEnabled(uriSet.size() < maxChoose);
                }
            } else {
                gridViewHolder.albumGridItemCheckView.setVisibility(View.GONE);
            }

            gridViewHolder.albumGridItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (maxChoose == TYPE_SINGLE_CHOOSE) {
                            listener.onItemClick(uri);
                        } else {
                            if (uriSet.contains(uri)) {
                                uriSet.remove(uri);
                                listener.onItemCancelClick(uri);
                                gridViewHolder.albumGridItemCheckView.setCheckedNum(CheckView.UNCHECKED);

                            } else {
                                if (uriSet.size() < maxChoose) {
                                    uriSet.add(uri);
                                    listener.onItemClick(uri);
                                    gridViewHolder.albumGridItemCheckView.setCheckedNum(uriSet.indexOf(uri) + 1);
                                } else {
                                    Toast.makeText(activity, "您已经选择了" + maxChoose + "张照片了！", Toast.LENGTH_SHORT).show();
                                }
                            }
                            notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }


    private class GridViewHolder extends RecyclerView.ViewHolder {
        GlideImageView albumGridItemImageView;
        CheckView albumGridItemCheckView;
        Button albumGridItemButton;

        GridViewHolder(View view) {
            super(view);
            this.albumGridItemImageView = (GlideImageView) view.findViewById(R.id.albumGridItemImageView);
            this.albumGridItemCheckView = (CheckView) view.findViewById(R.id.albumGridItemCheckView);
            this.albumGridItemButton = (Button) view.findViewById(R.id.albumGridItemButton);
        }
    }

    private class CameraViewHolder extends RecyclerView.ViewHolder {

        Button albumGridItemCameraButton;

        CameraViewHolder(View view) {
            super(view);
            this.albumGridItemCameraButton = (Button) view.findViewById(R.id.albumGridItemCameraButton);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onCameraClick();

        void onItemClick(Uri uri);

        void onItemCancelClick(Uri uri);
    }

}
