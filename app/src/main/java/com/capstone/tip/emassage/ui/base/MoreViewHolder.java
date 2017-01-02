package com.capstone.tip.emassage.ui.base;

import android.support.v7.widget.RecyclerView;

import com.capstone.tip.emassage.databinding.ItemMoreBinding;

/**
 * Created by Cholo Mia on 12/22/2016.
 */

public class MoreViewHolder extends RecyclerView.ViewHolder {
    public ItemMoreBinding itemMoreBinding;

    public MoreViewHolder(ItemMoreBinding itemMoreBinding) {
        super(itemMoreBinding.getRoot());
        this.itemMoreBinding = itemMoreBinding;
    }
}
