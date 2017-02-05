package com.capstone.tip.emassage.ui.category;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Endpoints;
import com.capstone.tip.emassage.databinding.ItemCategoryBinding;
import com.capstone.tip.emassage.model.data.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private List<Category> categories;
    private CategoryView categoryView;

    public CategoryListAdapter(CategoryView categoryView) {
        this.categoryView = categoryView;
        categories = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCategoryBinding itemCategoryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_category, parent, false);
        return new ViewHolder(itemCategoryBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.itemCategoryBinding.setCategory(categories.get(position));
        //holder.itemCategoryBinding.setView(categoryView);
        /*Glide.with(holder.itemView.getContext())
                .load(Endpoints.BASE_URL + categories.get(position).getCoverImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.itemCategoryBinding.imageCategory);*/
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
        private ItemCategoryBinding itemCategoryBinding;

        public ViewHolder(ItemCategoryBinding itemCategoryBinding) {
            super(itemCategoryBinding.getRoot());
            this.itemCategoryBinding = itemCategoryBinding;
        }
    }
}
