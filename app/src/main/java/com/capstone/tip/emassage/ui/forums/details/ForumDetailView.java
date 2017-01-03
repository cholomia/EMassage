package com.capstone.tip.emassage.ui.forums.details;

import com.capstone.tip.emassage.model.data.Forum;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 12/27/2016.
 */

public interface ForumDetailView extends MvpView {
    void setForum(Forum forum);

    void onSend(Forum forum);

    void showMessage(String message);

    void startLoading();

    void stopLoading();

    void deleteForumSuccessful();
}
