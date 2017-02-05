package com.capstone.tip.emassage.ui.gallery.list;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemGalleryBinding;
import com.capstone.tip.emassage.model.data.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.ViewHolder> {

    private List<Category> categories;
    private GalleryListView view;

    public GalleryListAdapter(GalleryListView view) {
        this.view = view;
        categories = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGalleryBinding itemGalleryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_gallery, parent, false);
        return new ViewHolder(itemGalleryBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemGalleryBinding.setCategory(categories.get(position));
        holder.itemGalleryBinding.setView(view);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemGalleryBinding itemGalleryBinding;

        public ViewHolder(ItemGalleryBinding itemGalleryBinding) {
            super(itemGalleryBinding.getRoot());
            this.itemGalleryBinding = itemGalleryBinding;
        }
    }
}
