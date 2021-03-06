package com.capstone.tip.emassage.ui.category;

import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.model.pojo.LessonGroup;
import com.capstone.tip.emassage.ui.base.LessonListView;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

@SuppressWarnings("WeakerAccess")
public interface CategoryView extends MvpView, LessonListView {

    void onCategoryItemClicked(Category category);

    void onRefresh();

    void startLoading();

    void stopLoading();

    void showAlert(String message);

    void setLessonGroups(List<LessonGroup> lessonGroups);
}
