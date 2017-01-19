package com.capstone.tip.emassage.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Cholo Mia on 12/27/2016.
 */

public class Comment extends RealmObject {

    @PrimaryKey
    private int id;

    private int forum;
    private String username;
    private String body;
    private Date created;
    private Date updated;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("my_vote")
    private int myVote;
    private double points;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getForum() {
        return forum;
    }

    public void setForum(int forum) {
        this.forum = forum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getMyVote() {
        return myVote;
    }

    public void setMyVote(int myVote) {
        this.myVote = myVote;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
