package com.capstone.tip.emassage.ui.forums;

import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.ui.base.VoteView;
import com.capstone.tip.emassage.ui.base.MoreListView;

import java.util.List;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public interface ForumsView extends VoteView, MoreListView {

    void onAddClicked();

    void stopLoading();

    void showMessage(String message);

    void addNext(String nextUrl);

    void onForumClicked(Forum forum);

    void setForums(List<Forum> forumList);

}
