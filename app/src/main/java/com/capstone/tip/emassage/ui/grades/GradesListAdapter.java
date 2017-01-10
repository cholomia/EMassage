package com.capstone.tip.emassage.ui.grades;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemDisplayGradeCategoryBinding;
import com.capstone.tip.emassage.databinding.ItemDisplayGradeCourseBinding;
import com.capstone.tip.emassage.databinding.ItemDisplayGradeLessonBinding;
import com.capstone.tip.emassage.model.pojo.DisplayGrade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 10/01/2017
 */

public class GradesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DisplayGrade> displayGrades;
    private GradesView gradesView;

    public GradesListAdapter(GradesView gradesView) {
        this.gradesView = gradesView;
        displayGrades = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return displayGrades.get(position).getView();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case DisplayGrade.VIEW_COURSE:
                ItemDisplayGradeCourseBinding itemDisplayGradeCourseBinding = DataBindingUtil
                        .inflate(LayoutInflater.from(parent.getContext()),
                                R.layout.item_display_grade_course, parent, false);
                return new CourseViewHolder(itemDisplayGradeCourseBinding);
            case DisplayGrade.VIEW_CATEGORY:
                ItemDisplayGradeCategoryBinding itemDisplayGradeCategoryBinding = DataBindingUtil
                        .inflate(LayoutInflater.from(parent.getContext()),
                                R.layout.item_display_grade_category, parent, false);
                return new CategoryViewHolder(itemDisplayGradeCategoryBinding);
            case DisplayGrade.VIEW_LESSON:
                ItemDisplayGradeLessonBinding itemDisplayGradeLessonBinding = DataBindingUtil
                        .inflate(LayoutInflater.from(parent.getContext()),
                                R.layout.item_display_grade_lesson, parent, false);
                return new LessonViewHolder(itemDisplayGradeLessonBinding);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case DisplayGrade.VIEW_COURSE:
                CourseViewHolder courseViewHolder = (CourseViewHolder) holder;
                courseViewHolder.itemDisplayGradeCourseBinding
                        .setDisplayGrade(displayGrades.get(position));
                break;
            case DisplayGrade.VIEW_CATEGORY:
                CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
                categoryViewHolder.itemDisplayGradeCategoryBinding
                        .setDisplayGrade(displayGrades.get(position));
                break;
            case DisplayGrade.VIEW_LESSON:
                LessonViewHolder lessonViewHolder = (LessonViewHolder) holder;
                lessonViewHolder.itemDisplayGradeLessonBinding
                        .setDisplayGrade(displayGrades.get(position));
                break;
        }
    }

    public void setDisplayGrades(List<DisplayGrade> displayGrades) {
        this.displayGrades.clear();
        this.displayGrades.addAll(displayGrades);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return displayGrades.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        private ItemDisplayGradeCourseBinding itemDisplayGradeCourseBinding;

        public CourseViewHolder(ItemDisplayGradeCourseBinding itemDisplayGradeCourseBinding) {
            super(itemDisplayGradeCourseBinding.getRoot());
            this.itemDisplayGradeCourseBinding = itemDisplayGradeCourseBinding;
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ItemDisplayGradeCategoryBinding itemDisplayGradeCategoryBinding;

        public CategoryViewHolder(ItemDisplayGradeCategoryBinding itemDisplayGradeCategoryBinding) {
            super(itemDisplayGradeCategoryBinding.getRoot());
            this.itemDisplayGradeCategoryBinding = itemDisplayGradeCategoryBinding;
        }
    }

    public class LessonViewHolder extends RecyclerView.ViewHolder {
        private ItemDisplayGradeLessonBinding itemDisplayGradeLessonBinding;

        public LessonViewHolder(ItemDisplayGradeLessonBinding itemDisplayGradeLessonBinding) {
            super(itemDisplayGradeLessonBinding.getRoot());
            this.itemDisplayGradeLessonBinding = itemDisplayGradeLessonBinding;
        }
    }

}
