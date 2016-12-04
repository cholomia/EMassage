package com.capstone.tip.emassage.model.data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class Lesson extends RealmObject {

    @PrimaryKey
    private int id;
    private String title;
    private int sequence;
    private String coverImage;
    private String pdf;
    private int category;
    private RealmList<Question> questions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public RealmList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(RealmList<Question> questions) {
        this.questions = questions;
    }
}
