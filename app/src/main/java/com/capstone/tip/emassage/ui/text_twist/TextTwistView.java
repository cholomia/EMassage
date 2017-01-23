package com.capstone.tip.emassage.ui.text_twist;

import com.capstone.tip.emassage.model.data.Twist;
import com.capstone.tip.emassage.model.pojo.Letter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 1/22/2017.
 */

public interface TextTwistView extends MvpView {
    void startLoading();

    void stopLoading();

    void showAlert(String message);

    void initData(int size);

    void onRefresh();

    void onFinish();

    void setTwist(Twist twist);

    void onTwist();

    void onEnter();

    void onLetterClicked(Letter letter, int position, boolean choice);
}
