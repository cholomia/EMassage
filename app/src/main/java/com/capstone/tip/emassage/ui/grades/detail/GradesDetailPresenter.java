package com.capstone.tip.emassage.ui.grades.detail;

import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.model.data.Lesson;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.ui.base.GradesSavePresenter;

import java.text.DecimalFormat;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.Credentials;

/**
 * Created by Cholo Mia on 3/4/2017.
 */

public class GradesDetailPresenter extends GradesSavePresenter<GradesDetailView> {

    private Realm realm;
    private User user;
    private RealmResults<Grade> gradeRealmResults;

    void start(int lessonId) {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        Lesson lesson = realm.where(Lesson.class).equalTo("id", lessonId).findFirst();
        if (lesson != null) {
            getView().setLessonTitle(lesson.getTitle());
        }
        gradeRealmResults = realm.where(Grade.class)
                .equalTo("lesson", lessonId)
                .findAllSortedAsync("tryCount", Sort.ASCENDING);
        gradeRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Grade>>() {
            @Override
            public void onChange(RealmResults<Grade> element) {
                if (gradeRealmResults.isValid() && gradeRealmResults.isLoaded()) {
                    getView().setGradesList(realm.copyFromRealm(gradeRealmResults));
                    getView().setFormattedAverage(formattedAverage());
                    getView().setNeedToSave(isNeedToSave());
                }
            }
        });
    }

    void stop() {
        gradeRealmResults.removeChangeListeners();
        realm.close();
    }

    public String formattedAverage() {
        if (gradeRealmResults.size() > 0) {
            double ave = gradeRealmResults.get(gradeRealmResults.size() - 1).average();
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(ave) + "%";
        }
        return "N/A";
    }

    public boolean isNeedToSave() {
        for (Grade grade : gradeRealmResults) {
            if (!grade.isSaved()) return true;
        }
        return false;
    }

    public void saveGrades() {
        for (Grade grade : gradeRealmResults)
            saveGrade(grade, Credentials.basic(user.getUsername(), user.getPassword()));
    }
}
