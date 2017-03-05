package com.capstone.tip.emassage.ui.grades;

import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.model.pojo.DisplayGrade;
import com.capstone.tip.emassage.model.pojo.LessonGroup;
import com.capstone.tip.emassage.ui.base.GradesSaveView;
import com.capstone.tip.emassage.ui.base.LessonListView;

import java.util.List;

/**
 * @author pocholomia
 * @since 10/01/2017
 */

public interface GradesView extends GradesSaveView, LessonListView {
    void setDisplayGradeList(List<DisplayGrade> displayGrades);

    void onGradeSave(List<Grade> grades);

    void setLessonGroupList(List<LessonGroup> lessonGroups);

    @Override
    void onLessonsItemClicked(int id);

    @Override
    void startLoading();

    @Override
    void stopLoading();

    @Override
    void showAlert(String message);
}
