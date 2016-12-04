package com.capstone.tip.emassage.ui.category;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class CategoryViewState implements RestorableViewState<CategoryView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<CategoryView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(CategoryView view, boolean retained) {

    }
}
