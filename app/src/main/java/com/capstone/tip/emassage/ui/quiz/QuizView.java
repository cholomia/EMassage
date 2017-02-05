package com.capstone.tip.emassage.ui.quiz;

import com.capstone.tip.emassage.model.data.Question;
import com.capstone.tip.emassage.model.pojo.UserAnswer;
import com.capstone.tip.emassage.ui.base.GradesSaveView;

import java.util.List;

/**
 * @author pocholomia
 * @since 05/12/2016
 */

public interface QuizView extends GradesSaveView {

    void onPrevious();

    void onNext();

    void restoreData(int counter, List<Question> questionList, List<UserAnswer> userAnswerList);

    void onViewReference(int page);
}
