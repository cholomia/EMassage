package com.capstone.tip.emassage.ui.lessons.detail;

import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.model.data.Lesson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author pocholomia
 * @since 02/02/2017
 */

public class LessonDetailPresenter extends MvpNullObjectBasePresenter<LessonDetailView> {

    private Realm realm;
    private Lesson lesson;

    void onStart(int id) {
        realm = Realm.getDefaultInstance();
        lesson = realm.where(Lesson.class).equalTo(Constants.ID, id).findFirstAsync();
        lesson.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (lesson.isLoaded() && lesson.isValid()) {
                    getView().setLesson(lesson);
                }
            }
        });
    }

    void onStop() {
        lesson.removeChangeListeners();
        realm.close();
    }

    public void getNextLesson(int id) {
        RealmResults<Category> categories = realm.where(Category.class).findAllSorted("sequence", Sort.ASCENDING);
        for (int i = 0; i < categories.size(); i++) {
            for (int j = 0; j < categories.get(i).getLessons().size(); j++) {
                Lesson lesson = categories.sort("sequence").get(i).getLessons().sort("sequence").get(j);
                if (lesson.getId() == id) {
                    if (j == categories.sort("sequence").get(i).getLessons().size() - 1) {
                        if (i == categories.size() - 1) {
                            getView().setNoNextLesson();
                        } else {
                            getView().setNextLesson(categories.sort("sequence").get(i + 1).getLessons().sort("sequence").get(0));
                        }
                    } else {
                        Lesson nextLesson = categories.sort("sequence").get(i).getLessons().sort("sequence").get(j + 1);
                        getView().setNextLesson(nextLesson);
                    }
                }
            }
        }
    }

    public boolean hasTakenQuiz() {
        Grade grade = realm.where(Grade.class).equalTo("lesson", lesson.getId()).findFirst();
        return grade != null;
    }
}
