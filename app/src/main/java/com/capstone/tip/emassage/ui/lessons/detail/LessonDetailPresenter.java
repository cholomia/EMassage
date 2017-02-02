package com.capstone.tip.emassage.ui.lessons.detail;

import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Lesson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

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
                    getView().setLesson(realm.copyFromRealm(lesson));
                }
            }
        });
    }

    void onStop() {
        lesson.removeChangeListeners();
        realm.close();
    }
}
