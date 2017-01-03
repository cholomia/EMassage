package com.capstone.tip.emassage.ui.forums.form;

import com.capstone.tip.emassage.model.data.Forum;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 12/22/2016.
 */

public interface ForumFormView extends MvpView {

    void onSave();

    void showMessage(String message);

    void stopLoading();

    void startLoading();

    void saveSuccess();

    void setForum(Forum forum);
}
