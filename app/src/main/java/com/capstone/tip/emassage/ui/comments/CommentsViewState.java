package com.capstone.tip.emassage.ui.comments;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 12/27/2016.
 */

public class CommentsViewState implements RestorableViewState<CommentsView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<CommentsView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(CommentsView view, boolean retained) {

    }
}
