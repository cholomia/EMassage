package com.capstone.tip.emassage.ui.forums.details;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 12/27/2016.
 */

public class ForumDetailViewState implements RestorableViewState<ForumDetailView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<ForumDetailView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(ForumDetailView view, boolean retained) {

    }
}
