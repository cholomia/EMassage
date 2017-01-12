package com.capstone.tip.emassage.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Cholo Mia on 12/22/2016.
 */

public class Forum extends RealmObject {

    @PrimaryKey
    private int id;

    private String username;
    private String title;
    private String content;
    private Date created;
    private Date updated;
    @SerializedName("comment_count")
    private int commentCounts;
    @SerializedName("vote_count")
    private int voteCounts;
    @SerializedName("my_vote")
    private int myVote;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public int getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(int commentCounts) {
        this.commentCounts = commentCounts;
    }

    public int getVoteCounts() {
        return voteCounts;
    }

    public void setVoteCounts(int voteCounts) {
        this.voteCounts = voteCounts;
    }

    public int getMyVote() {
        return myVote;
    }

    public void setMyVote(int myVote) {
        this.myVote = myVote;
    }
}
