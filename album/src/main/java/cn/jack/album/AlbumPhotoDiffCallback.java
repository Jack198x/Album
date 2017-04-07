package cn.jack.album;

import android.net.Uri;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Jack on 2016/12/15.
 */

public class AlbumPhotoDiffCallback extends DiffUtil.Callback {

    private List<Uri> oldData, newData;

    public AlbumPhotoDiffCallback(List<Uri> oldData, List<Uri> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData != null ? oldData.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newData != null ? newData.size() : 0;
    }


    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Uri oldUri = oldData.get(oldItemPosition);
        Uri newUri = newData.get(newItemPosition);
        String oldPath = TextUtils.isEmpty(oldUri.getPath()) ? "" : oldUri.getPath();
        String newPath = TextUtils.isEmpty(newUri.getPath()) ? "" : newUri.getPath();
        return oldPath.equals(newPath);
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }
}
