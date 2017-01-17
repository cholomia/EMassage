package com.capstone.tip.emassage.ui.video_simulation;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * @author pocholomia
 * @since 17/01/2017
 */

public class VideoSimulationViewState implements RestorableViewState<VideoSimulationView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<VideoSimulationView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(VideoSimulationView view, boolean retained) {

    }
}
