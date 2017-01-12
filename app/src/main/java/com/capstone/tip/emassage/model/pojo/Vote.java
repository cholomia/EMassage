package com.capstone.tip.emassage.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author pocholomia
 * @since 12/01/2017
 */

public class Vote {

    @SerializedName("vote_id")
    private String voteId;
    @SerializedName("forum")
    private int forumId;
    @SerializedName("vote")
    private int vote;

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

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
}
