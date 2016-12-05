package com.capstone.tip.emassage.ui.quiz;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Question;
import com.capstone.tip.emassage.model.pojo.UserAnswer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 05/12/2016
 */

public class QuizViewState implements RestorableViewState<QuizView> {

    private int counter;
    private List<UserAnswer> userAnswerList;
    private List<Question> questionList;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putInt(Constants.COUNTER, counter);
        // todo: save identification input and selected on multiple choice
        Gson gson = new GsonBuilder().create();
        ArrayList<String> questionJson = new ArrayList<>();
        for (Question question : questionList) {
            questionJson.add(gson.toJson(question));
        }
        ArrayList<String> userAnswerJson = new ArrayList<>();
        for (UserAnswer userAnswer : userAnswerList) {
            userAnswerJson.add(gson.toJson(userAnswer));
        }
        out.putStringArrayList(Constants.QUESTIONS, questionJson);
        out.putStringArrayList(Constants.ANSWERS, userAnswerJson);
    }

    @Override
    public RestorableViewState<QuizView> restoreInstanceState(Bundle in) {
        counter = in.getInt(Constants.COUNTER, 0);
        // todo: get identification input and selected on multiple choice
        Gson gson = new GsonBuilder().create();
        ArrayList<String> userAnswerJson = in.getStringArrayList(Constants.ANSWERS);
        ArrayList<String> questionJson = in.getStringArrayList(Constants.QUESTIONS);
        questionList = new ArrayList<>();
        for (String question : questionJson != null ? questionJson : new ArrayList<String>()) {
            questionList.add(gson.fromJson(question, Question.class));
        }
        userAnswerList = new ArrayList<>();
        for (String answer : userAnswerJson != null ? userAnswerJson : new ArrayList<String>()) {
            userAnswerList.add(gson.fromJson(answer, UserAnswer.class));
        }
        return this;
    }

    @Override
    public void apply(QuizView view, boolean retained) {
        view.restoreData(counter, questionList, userAnswerList);
    }

    void setCounter(int counter) {
        this.counter = counter;
    }

    int getCounter() {
        return counter;
    }

    void decrementCounter() {
        counter--;
    }

    void incrementCounter() {
        counter++;
    }

    void setUserAnswerList(List<UserAnswer> userAnswerList) {
        this.userAnswerList = userAnswerList;
    }

    void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

}
