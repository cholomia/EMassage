package com.capstone.tip.emassage.ui.announcement.list;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class AnnouncementListViewState implements RestorableViewState<AnnouncementListView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<AnnouncementListView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(AnnouncementListView view, boolean retained) {

    }
}
