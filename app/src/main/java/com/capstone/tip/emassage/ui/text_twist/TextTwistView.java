package com.capstone.tip.emassage.ui.text_twist;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 1/22/2017.
 */

public interface TextTwistView extends MvpView {
    void startLoading();

    void stopLoading();

    void showAlert(String message);
}
