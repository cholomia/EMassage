package com.capstone.tip.emassage.model.response;

import com.capstone.tip.emassage.model.data.Comment;
import com.capstone.tip.emassage.model.data.Forum;
import com.google.gson.annotations.SerializedName;

/**
 * @author pocholomia
 * @since 19/01/2017
 */

public class CommentVote {

    @SerializedName("comment")
    private int commentId;
    @SerializedName("vote")
    private int vote;
    @SerializedName("comment_votes")
    private Comment comment;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
