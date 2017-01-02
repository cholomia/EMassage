package com.capstone.tip.emassage.ui.comments;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemCommentBinding;
import com.capstone.tip.emassage.databinding.ItemMoreBinding;
import com.capstone.tip.emassage.model.data.Comment;
import com.capstone.tip.emassage.ui.base.MoreViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 12/29/2016.
 */

public class CommentsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;

    private List<Comment> commentList;
    private String nextUrl;
    private CommentsView view;
    private boolean loading;

    public CommentsListAdapter(CommentsView view) {
        this.view = view;
        commentList = new ArrayList<>();
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
                ItemCommentBinding itemCommentBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.item_comment, parent, false);
                return new CommentViewHolder(itemCommentBinding);
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
                CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
                commentViewHolder.itemCommentBinding.setComment(commentList.get(position));
                break;
            case VIEW_TYPE_MORE:
                MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
                moreViewHolder.itemMoreBinding.setUrl(nextUrl);
                moreViewHolder.itemMoreBinding.setView(view);
                moreViewHolder.itemMoreBinding.setLoading(loading);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count = commentList.size();
        if (nextUrl != null && !nextUrl.isEmpty())
            count++;
        return count;
    }

    public void setComments(List<Comment> commentList) {
        this.commentList.clear();
        this.commentList.addAll(commentList);
        notifyDataSetChanged();
    }

    public void clear() {
        commentList.clear();
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

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private ItemCommentBinding itemCommentBinding;

        public CommentViewHolder(ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.getRoot());
            this.itemCommentBinding = itemCommentBinding;
        }
    }


}
