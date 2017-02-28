package com.capstone.tip.emassage.ui.lessons.detail;

import android.view.View;

import com.capstone.tip.emassage.model.data.Lesson;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 02/02/2017
 */

public interface LessonDetailView extends MvpView {
    void setLesson(Lesson lesson);

    void onPrevious();

    void onNext();

    void onPopTextToSpeech(View view, String stringToTextToSpeech);

    void onMenu();

    void onTakeQuiz();

    void onViewVideo();

    void setNextLesson(Lesson nextLesson);

    void setNoNextLesson();
}
