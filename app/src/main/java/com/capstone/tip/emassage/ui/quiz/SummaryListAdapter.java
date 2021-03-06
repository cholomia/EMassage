package com.capstone.tip.emassage.ui.quiz;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemUserAnswerBinding;
import com.capstone.tip.emassage.model.pojo.UserAnswer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 22/11/2016
 * Adapter for the summary list RecyclerView
 */

public class SummaryListAdapter extends RecyclerView.Adapter<SummaryListAdapter.ViewHolder> {

    private final List<UserAnswer> userAnswerList;
    private QuizView view;

    /**
     * Constructor and init the list
     */
    public SummaryListAdapter(QuizView view) {
        this.view = view;
        userAnswerList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemUserAnswerBinding itemUserAnswerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_user_answer,
                parent,
                false);
        return new ViewHolder(itemUserAnswerBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserAnswer userAnswer = userAnswerList.get(position);
        holder.itemUserAnswerBinding.setAnswer(userAnswer);
        String strItemNum = (position + 1) + ".)"; // add 1 as index starts w/ 0
        holder.itemUserAnswerBinding.txtItemNum.setText(strItemNum);
        holder.itemUserAnswerBinding.setView(view);
    }

    @Override
    public int getItemCount() {
        return userAnswerList.size();
    }

    /**
     * @param userAnswerList list of user answers to display
     */
    public void setUserAnswerList(List<UserAnswer> userAnswerList) {
        this.userAnswerList.clear();
        this.userAnswerList.addAll(userAnswerList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserAnswerBinding itemUserAnswerBinding;

        ViewHolder(ItemUserAnswerBinding itemUserAnswerBinding) {
            super(itemUserAnswerBinding.getRoot());
            this.itemUserAnswerBinding = itemUserAnswerBinding;
        }
    }
}
