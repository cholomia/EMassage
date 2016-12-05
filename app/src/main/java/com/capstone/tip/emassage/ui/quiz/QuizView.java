package com.capstone.tip.emassage.ui.quiz;

import com.capstone.tip.emassage.model.data.Question;
import com.capstone.tip.emassage.model.pojo.UserAnswer;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * @author pocholomia
 * @since 05/12/2016
 */

public interface QuizView extends MvpView {

    void onPrevious();

    void onNext();

    void restoreData(int counter, List<Question> questionList, List<UserAnswer> userAnswerList);
}
