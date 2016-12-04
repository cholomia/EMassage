package com.capstone.tip.emassage.ui.forums;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class ForumsViewState implements RestorableViewState<ForumsView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<ForumsView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(ForumsView view, boolean retained) {

    }
}
