package com.capstone.tip.emassage.ui.gallery.detail;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemGalleryDetailBinding;
import com.capstone.tip.emassage.model.data.Gallery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class GalleryDetailAdapter extends RecyclerView.Adapter<GalleryDetailAdapter.ViewHolder> {

    private List<Gallery> galleries;
    private GalleryDetailView view;

    public GalleryDetailAdapter(GalleryDetailView view) {
        this.view = view;
        galleries = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGalleryDetailBinding itemGalleryDetailBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_gallery_detail, parent, false);
        return new ViewHolder(itemGalleryDetailBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemGalleryDetailBinding.setView(view);
        holder.itemGalleryDetailBinding.setGallery(galleries.get(position));
        Glide.with(holder.itemView.getContext())
                .load(galleries.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.itemGalleryDetailBinding.image);
    }

    @Override
    public int getItemCount() {
        return galleries.size();
    }

    public void setGalleries(List<Gallery> galleries) {
        this.galleries.clear();
        this.galleries.addAll(galleries);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemGalleryDetailBinding itemGalleryDetailBinding;

        public ViewHolder(ItemGalleryDetailBinding itemGalleryDetailBinding) {
            super(itemGalleryDetailBinding.getRoot());
            this.itemGalleryDetailBinding = itemGalleryDetailBinding;
        }
    }
}
