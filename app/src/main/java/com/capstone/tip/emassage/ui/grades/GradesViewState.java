package com.capstone.tip.emassage.ui.grades;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * @author pocholomia
 * @since 10/01/2017
 */

public class GradesViewState implements RestorableViewState<GradesView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<GradesView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(GradesView view, boolean retained) {

    }
}
