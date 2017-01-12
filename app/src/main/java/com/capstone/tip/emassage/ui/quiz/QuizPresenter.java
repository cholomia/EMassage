package com.capstone.tip.emassage.ui.quiz;

import android.util.Log;

import com.capstone.tip.emassage.model.data.Choice;
import com.capstone.tip.emassage.model.data.Question;
import com.capstone.tip.emassage.ui.base.GradesSavePresenter;
import com.capstone.tip.emassage.ui.base.GradesSaveView;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;

/**
 * @author pocholomia
 * @since 05/12/2016
 */

public class QuizPresenter extends GradesSavePresenter<QuizView> {
    public List<Question> getShuffledQuestionList(List<Question> questions) {
        List<Question> questionList = new ArrayList<>();
        for (Question question : questions) {
            RealmList<Choice> choices = question.getChoices();
            Collections.shuffle(choices);
            question.setChoices(choices);
            questionList.add(question);
        }
        Collections.shuffle(questionList);
        return questionList;
    }

    /**
     * @param score raw score
     * @param items total number of items
     * @return return average using score/items * 50 + 50
     */
    double getAverage(int score, int items) {
        double ave = (((double) score / (double) items) * 50.0) + 50.0;
        double a = score / items;
        double b = a * 50;
        double c = b + 50;
        return ave;
    }
}
