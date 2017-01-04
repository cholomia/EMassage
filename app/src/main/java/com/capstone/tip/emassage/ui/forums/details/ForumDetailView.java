package com.capstone.tip.emassage.ui.forums.details;

import com.capstone.tip.emassage.model.data.Comment;
import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.ui.base.VoteView;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 12/27/2016.
 */

public interface ForumDetailView extends VoteView {
    void setForum(Forum forum);

    void onSend(Forum forum);

    void showMessage(String message);

    void startLoading();

    void stopLoading();

    void deleteForumSuccessful();

    void onEditComment(Comment comment);

    void onCommentSaved();
}
