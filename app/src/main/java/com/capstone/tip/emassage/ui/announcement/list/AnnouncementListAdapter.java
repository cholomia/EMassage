package com.capstone.tip.emassage.ui.announcement.list;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemAnnouncementBinding;
import com.capstone.tip.emassage.model.data.Announcement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class AnnouncementListAdapter extends RecyclerView.Adapter<AnnouncementListAdapter.ViewHolder> {

    private List<Announcement> announcements;
    private AnnouncementListView view;

    public AnnouncementListAdapter(AnnouncementListView view) {
        announcements = new ArrayList<>();
        this.view = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAnnouncementBinding itemAnnouncementBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_announcement, parent, false);
        return new ViewHolder(itemAnnouncementBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemAnnouncementBinding.setAnnouncement(announcements.get(position));
        holder.itemAnnouncementBinding.setView(view);
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements.clear();
        this.announcements.addAll(announcements);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemAnnouncementBinding itemAnnouncementBinding;

        public ViewHolder(ItemAnnouncementBinding itemAnnouncementBinding) {
            super(itemAnnouncementBinding.getRoot());
            this.itemAnnouncementBinding = itemAnnouncementBinding;
        }
    }
}
