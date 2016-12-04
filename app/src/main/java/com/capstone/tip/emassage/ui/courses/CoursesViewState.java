package com.capstone.tip.emassage.ui.courses;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class CoursesViewState implements RestorableViewState<CoursesView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<CoursesView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(CoursesView view, boolean retained) {

    }
}
