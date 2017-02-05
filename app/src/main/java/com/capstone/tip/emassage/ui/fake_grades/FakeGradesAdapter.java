package com.capstone.tip.emassage.ui.fake_grades;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemFakeGradesBinding;
import com.capstone.tip.emassage.model.pojo.FakeGrade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class FakeGradesAdapter extends RecyclerView.Adapter<FakeGradesAdapter.ViewHolder> {

    private List<FakeGrade> fakeGrades;

    public FakeGradesAdapter() {
        fakeGrades = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFakeGradesBinding itemFakeGradesBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_fake_grades, parent, false);
        return new ViewHolder(itemFakeGradesBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemFakeGradesBinding.setFakeGrade(fakeGrades.get(position));
    }

    @Override
    public int getItemCount() {
        return fakeGrades.size();
    }

    public void setFakeGrades(List<FakeGrade> fakeGrades) {
        this.fakeGrades.clear();
        this.fakeGrades.addAll(fakeGrades);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemFakeGradesBinding itemFakeGradesBinding;

        public ViewHolder(ItemFakeGradesBinding itemFakeGradesBinding) {
            super(itemFakeGradesBinding.getRoot());
            this.itemFakeGradesBinding = itemFakeGradesBinding;
        }
    }
}
