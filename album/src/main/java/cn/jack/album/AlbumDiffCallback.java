package cn.jack.album;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Jack on 2016/12/15.
 */

public class AlbumDiffCallback extends DiffUtil.Callback {

    private List<AlbumModel> oldData, newData;

    public AlbumDiffCallback(List<AlbumModel> oldData, List<AlbumModel> newData) {
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
        AlbumModel oldAlbum = oldData.get(oldItemPosition);
        AlbumModel newAlbum = newData.get(newItemPosition);
        String oldAlbumId = TextUtils.isEmpty(oldAlbum.getAlbumId()) ? "" : oldAlbum.getAlbumId();
        String newAlbumId = TextUtils.isEmpty(newAlbum.getAlbumId()) ? "" : newAlbum.getAlbumId();
        return oldAlbumId.equals(newAlbumId);
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        AlbumModel oldAlbum = oldData.get(oldItemPosition);
        AlbumModel newAlbum = newData.get(newItemPosition);
        String oldCoverPath = oldAlbum.getCoverUri().getPath();
        String newCoverPath = newAlbum.getCoverUri().getPath();
        return oldCoverPath.equals(newCoverPath);
    }
}
