package com.capstone.tip.emassage.ui.lessons;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 12/5/2016.
 */

public class LessonsViewState implements RestorableViewState<LessonsView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<LessonsView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(LessonsView view, boolean retained) {

    }
}
