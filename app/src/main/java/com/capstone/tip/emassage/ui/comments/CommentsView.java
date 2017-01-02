package com.capstone.tip.emassage.ui.comments;

import com.capstone.tip.emassage.model.data.Comment;
import com.capstone.tip.emassage.ui.base.MoreListView;

import java.util.List;

/**
 * Created by Cholo Mia on 12/27/2016.
 */

public interface CommentsView extends MoreListView {
    void setComments(List<Comment> comments);

    void stopLoading();

    void showMessage(String message);

    void addNext(String nextUrl);
}
