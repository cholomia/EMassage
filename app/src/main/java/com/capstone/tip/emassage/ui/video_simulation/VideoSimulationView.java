package com.capstone.tip.emassage.ui.video_simulation;

import com.capstone.tip.emassage.model.data.Video;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * @author pocholomia
 * @since 17/01/2017
 */

public interface VideoSimulationView extends MvpView {
    void setVideoList(List<Video> videos);

    void stopLoading();

    void showMessage(String message);

    void onVideoClicked(Video video);
}
