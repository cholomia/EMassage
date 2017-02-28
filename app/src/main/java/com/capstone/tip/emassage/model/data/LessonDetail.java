package com.capstone.tip.emassage.model.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 28/02/2017
 */

public class LessonDetail extends RealmObject {

    @PrimaryKey
    private int id;
    private int sequence;
    private String title;
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
