package com.capstone.tip.emassage.ui.fake_grades;

import com.capstone.tip.emassage.model.pojo.FakeGrade;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public interface FakeGradesView extends MvpView {
    void setFakeGrades(List<FakeGrade> fakeGrades);
}
