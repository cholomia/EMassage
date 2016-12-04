package com.capstone.tip.emassage.ui.courses;

import com.capstone.tip.emassage.model.data.Course;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public interface CoursesView extends MvpView {
    void startLoading();

    void stopLoading();

    void showAlert(String message);

    void onCourseItemClicked(Course course);

    void onRefresh();
}
