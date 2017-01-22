package com.capstone.tip.emassage.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 10/01/2017
 */

public class Grade extends RealmObject {

    @PrimaryKey
    private String id;
    private int lesson;
    @SerializedName("raw_score")
    private int rawScore;
    @SerializedName("item_count")
    private int itemCount;
    private boolean saved;
    @SerializedName("try_count")
    private int tryCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public int getRawScore() {
        return rawScore;
    }

    public void setRawScore(int rawScore) {
        this.rawScore = rawScore;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public int getTryCount() {
        return tryCount;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    /**
     * @return average of the grade score/items * 50 + 50
     */
    public double average() {
        return (((double) rawScore / (double) itemCount) * 50.0) + 50.0;
    }
}
