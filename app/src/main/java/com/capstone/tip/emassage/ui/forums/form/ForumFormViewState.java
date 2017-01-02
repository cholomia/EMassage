package com.capstone.tip.emassage.ui.forums.form;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 12/22/2016.
 */

public class ForumFormViewState implements RestorableViewState<ForumFormView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<ForumFormView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(ForumFormView view, boolean retained) {

    }
}
