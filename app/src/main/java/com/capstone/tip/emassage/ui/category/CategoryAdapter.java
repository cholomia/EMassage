package com.capstone.tip.emassage.ui.category;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemCategoryBinding;
import com.capstone.tip.emassage.databinding.ItemLessonBinding;
import com.capstone.tip.emassage.model.pojo.LessonGroup;
import com.capstone.tip.emassage.model.pojo.LessonParcelable;
import com.capstone.tip.emassage.ui.base.LessonListView;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class CategoryAdapter extends ExpandableRecyclerViewAdapter<CategoryAdapter.CategoryViewHolder, CategoryAdapter.LessonViewHolder> {

    private LessonListView mvpView;

    public CategoryAdapter(List<LessonGroup> groups, LessonListView mvpView) {
        super(groups);
        this.mvpView = mvpView;
    }

    @Override
    public CategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        ItemCategoryBinding itemCategoryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_category, parent, false);
        return new CategoryViewHolder(itemCategoryBinding);
    }

    @Override
    public LessonViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        ItemLessonBinding itemLessonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_lesson, parent, false);
        return new LessonViewHolder(itemLessonBinding);
    }

    @Override
    public void onBindGroupViewHolder(CategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.itemCategoryBinding.setCategory(group);
    }

    @Override
    public void onBindChildViewHolder(LessonViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        LessonParcelable lessonParcelable = ((LessonGroup) group).getItems().get(childIndex);
        holder.itemLessonBinding.setView(mvpView);
        holder.itemLessonBinding.setLesson(lessonParcelable);
    }

    class CategoryViewHolder extends GroupViewHolder {
        private ItemCategoryBinding itemCategoryBinding;

        CategoryViewHolder(ItemCategoryBinding itemCategoryBinding) {
            super(itemCategoryBinding.getRoot());
            this.itemCategoryBinding = itemCategoryBinding;
        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            itemCategoryBinding.listItemGenreArrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            itemCategoryBinding.listItemGenreArrow.setAnimation(rotate);
        }
    }

    class LessonViewHolder extends ChildViewHolder {
        private ItemLessonBinding itemLessonBinding;

        LessonViewHolder(ItemLessonBinding itemLessonBinding) {
            super(itemLessonBinding.getRoot());
            this.itemLessonBinding = itemLessonBinding;
        }
    }

}
