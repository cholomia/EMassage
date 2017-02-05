package com.capstone.tip.emassage.ui.gallery.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class GalleryDetailViewState implements RestorableViewState<GalleryDetailView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<GalleryDetailView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(GalleryDetailView view, boolean retained) {

    }
}
