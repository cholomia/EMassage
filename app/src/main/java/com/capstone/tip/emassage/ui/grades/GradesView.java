package com.capstone.tip.emassage.ui.grades;

import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.model.pojo.DisplayGrade;
import com.capstone.tip.emassage.ui.base.GradesSaveView;

import java.util.List;

/**
 * @author pocholomia
 * @since 10/01/2017
 */

public interface GradesView extends GradesSaveView {
    void setDisplayGradeList(List<DisplayGrade> displayGrades);

    void onGradeSave(Grade grade);

}
