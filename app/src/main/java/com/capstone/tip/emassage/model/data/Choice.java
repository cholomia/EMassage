package com.capstone.tip.emassage.model.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class Choice extends RealmObject {

    @PrimaryKey
    private int id;
    private String body;
    private int question;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }
}
