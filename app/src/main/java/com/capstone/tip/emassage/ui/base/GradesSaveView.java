package com.capstone.tip.emassage.ui.base;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 12/01/2017
 */

public interface GradesSaveView extends MvpView {
    void startLoading();

    void stopLoading();

    void showAlert(String message);
}
