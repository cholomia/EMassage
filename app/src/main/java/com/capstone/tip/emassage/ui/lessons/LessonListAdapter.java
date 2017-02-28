package com.capstone.tip.emassage.ui.lessons;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemLessonBinding;
import com.capstone.tip.emassage.model.data.Lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 12/5/2016.
 */

public class LessonListAdapter extends RecyclerView.Adapter<LessonListAdapter.ViewHolder> {

    private List<Lesson> lessons;

    public LessonListAdapter(LessonsView lessonsView) {
        LessonsView lessonsView1 = lessonsView;
        lessons = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLessonBinding itemLessonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_lesson, parent, false);
        return new ViewHolder(itemLessonBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.itemLessonBinding.setLesson(lessons.get(position));
        //holder.itemLessonBinding.setView(lessonsView);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons.clear();
        this.lessons.addAll(lessons);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemLessonBinding itemLessonBinding;

        public ViewHolder(ItemLessonBinding itemLessonBinding) {
            super(itemLessonBinding.getRoot());
            this.itemLessonBinding = itemLessonBinding;
        }
    }
}
