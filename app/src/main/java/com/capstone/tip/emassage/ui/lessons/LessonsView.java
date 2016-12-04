package com.capstone.tip.emassage.ui.lessons;

import com.capstone.tip.emassage.model.data.Lesson;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 12/5/2016.
 */

public interface LessonsView extends MvpView {

    void onLessonsItemClicked(Lesson lesson);
}
