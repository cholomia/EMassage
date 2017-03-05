package com.capstone.tip.emassage.ui.grades.detail;

import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.ui.base.GradesSaveView;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by Cholo Mia on 3/4/2017.
 */

public interface GradesDetailView extends MvpView, GradesSaveView {
    void setGradesList(List<Grade> grades);

    void setLessonTitle(String title);

    void setFormattedAverage(String formattedAverage);

    void setNeedToSave(boolean needToSave);
}
