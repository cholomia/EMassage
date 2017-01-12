package com.capstone.tip.emassage.ui.base;

import com.capstone.tip.emassage.model.data.Forum;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 04/01/2017
 */

public interface VoteView extends MvpView {

    void onUpVote(Forum forum);

    void onDownVote(Forum forum);

    void startProgressLoading();

    void stopProgressDialog();

    void showMessage(String message);
}
