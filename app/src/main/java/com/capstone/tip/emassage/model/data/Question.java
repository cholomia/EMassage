package com.capstone.tip.emassage.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class Question extends RealmObject {

    @PrimaryKey
    private int id;
    private String body;
    private String answer;
    private int lesson;
    @SerializedName("lesson_detail")
    private int lessonDetail;
    private RealmList<Choice> choices;

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public int getLessonDetail() {
        return lessonDetail;
    }

    public void setLessonDetail(int lessonDetail) {
        this.lessonDetail = lessonDetail;
    }

    public RealmList<Choice> getChoices() {
        return choices;
    }

    public void setChoices(RealmList<Choice> choices) {
        this.choices = choices;
    }
}
