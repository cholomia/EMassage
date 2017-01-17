package com.capstone.tip.emassage.model.pojo;

import com.capstone.tip.emassage.model.data.Forum;
import com.google.gson.annotations.SerializedName;

/**
 * @author pocholomia
 * @since 12/01/2017
 */

public class Vote {

    @SerializedName("forum")
    private int forumId;
    @SerializedName("vote")
    private int vote;
    @SerializedName("forum_votes")
    private Forum forum;

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }
}
