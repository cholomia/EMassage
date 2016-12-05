package com.capstone.tip.emassage.ui.quiz;

import com.capstone.tip.emassage.model.data.Choice;
import com.capstone.tip.emassage.model.data.Question;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;

/**
 * @author pocholomia
 * @since 05/12/2016
 */

public class QuizPresenter extends MvpNullObjectBasePresenter<QuizView> {
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
}
