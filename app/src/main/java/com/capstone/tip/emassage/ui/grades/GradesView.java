package com.capstone.tip.emassage.ui.grades;

import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.model.pojo.DisplayGrade;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * @author pocholomia
 * @since 10/01/2017
 */

public interface GradesView extends MvpView {
    void setDisplayGradeList(List<DisplayGrade> displayGrades);

    void startLoading();

    void stopLoading();

    void showAlert(String message);

    void onGradeSave(Grade grade);

}
