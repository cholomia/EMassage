package com.capstone.tip.emassage.ui.forums;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemForumBinding;
import com.capstone.tip.emassage.databinding.ItemMoreBinding;
import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.ui.base.MoreViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 12/22/2016.
 */

public class ForumsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;

    private List<Forum> forumList;
    private String nextUrl;
    private ForumsView forumsView;
    private boolean loading;

    public ForumsListAdapter(ForumsView forumsView) {
        this.forumsView = forumsView;
        forumList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (nextUrl != null && !nextUrl.isEmpty() && position == getItemCount() - 1)
            return VIEW_TYPE_MORE;
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_DEFAULT:
                ItemForumBinding itemForumBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.item_forum, parent, false);
                return new ForumViewHolder(itemForumBinding);
            case VIEW_TYPE_MORE:
                ItemMoreBinding itemMoreBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.item_more, parent, false);
                return new MoreViewHolder(itemMoreBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_DEFAULT:
                ForumViewHolder forumViewHolder = (ForumViewHolder) holder;
                forumViewHolder.itemForumBinding.setForum(forumList.get(position));
                forumViewHolder.itemForumBinding.setView(forumsView);
                forumViewHolder.itemForumBinding.setVoteView(forumsView);
                break;
            case VIEW_TYPE_MORE:
                MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
                moreViewHolder.itemMoreBinding.setUrl(nextUrl);
                moreViewHolder.itemMoreBinding.setView(forumsView);
                moreViewHolder.itemMoreBinding.setLoading(loading);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count = forumList.size();
        if (nextUrl != null && !nextUrl.isEmpty())
            count++;
        return count;
    }

    public void setForums(List<Forum> forumList) {
        this.forumList.clear();
        this.forumList.addAll(forumList);
        notifyDataSetChanged();
    }

    public void clear() {
        forumList.clear();
        notifyDataSetChanged();
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
        notifyDataSetChanged();
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyDataSetChanged();
    }

    public class ForumViewHolder extends RecyclerView.ViewHolder {
        private ItemForumBinding itemForumBinding;

        public ForumViewHolder(ItemForumBinding itemForumBinding) {
            super(itemForumBinding.getRoot());
            this.itemForumBinding = itemForumBinding;
        }
    }

}
