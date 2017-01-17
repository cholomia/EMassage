package com.capstone.tip.emassage.ui.video_simulation;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Endpoints;
import com.capstone.tip.emassage.databinding.ItemVideosBinding;
import com.capstone.tip.emassage.model.data.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 17/01/2017
 */

public class VideoSimulationListAdapter extends RecyclerView.Adapter<VideoSimulationListAdapter.ViewHolder> {

    private static final String TAG = VideoSimulationListAdapter.class.getSimpleName();
    private List<Video> videos;
    private VideoSimulationView view;

    public VideoSimulationListAdapter(VideoSimulationView view) {
        this.view = view;
        videos = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemVideosBinding itemVideosBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_videos, parent, false);
        return new ViewHolder(itemVideosBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemVideosBinding.setView(view);
        holder.itemVideosBinding.setVideo(videos.get(position));
        Glide.with(holder.itemView.getContext())
                .load(Endpoints.YOUTUBE_THUMBNAIL.replace("{code}", videos.get(position).getCode()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.d(TAG, "onResourceReady: " + model);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d(TAG, "onResourceReady: " + model);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.itemVideosBinding.imgVideoThumbnail);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void setVideoList(List<Video> videoList) {
        this.videos.clear();
        this.videos.addAll(videoList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemVideosBinding itemVideosBinding;

        public ViewHolder(ItemVideosBinding itemVideosBinding) {
            super(itemVideosBinding.getRoot());
            this.itemVideosBinding = itemVideosBinding;
        }
    }
}
