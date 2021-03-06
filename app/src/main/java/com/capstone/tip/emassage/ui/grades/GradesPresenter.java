package com.capstone.tip.emassage.ui.grades;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.model.data.Lesson;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.model.pojo.DisplayGrade;
import com.capstone.tip.emassage.model.pojo.LessonGroup;
import com.capstone.tip.emassage.model.pojo.LessonParcelable;
import com.capstone.tip.emassage.ui.base.GradesSavePresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pocholomia
 * @since 10/01/2017
 */

public class GradesPresenter extends GradesSavePresenter<GradesView> {

    private static final String TAG = GradesPresenter.class.getSimpleName();

    private Realm realm;
    private RealmResults<Category> categoryRealmResults;
    private RealmResults<Grade> gradeRealmResults;
    private User user;

    public void onStart() {
        realm = Realm.getDefaultInstance();

        user = realm.where(User.class).findFirst();

        categoryRealmResults = realm.where(Category.class).findAllSortedAsync(Constants.COL_SEQ);
        categoryRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Category>>() {
            @Override
            public void onChange(RealmResults<Category> element) {
                if (categoryRealmResults.isValid() && categoryRealmResults.isLoaded())
                    setLessonGroupList();
            }
        });

        gradeRealmResults = realm.where(Grade.class).findAllAsync();
        gradeRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Grade>>() {
            @Override
            public void onChange(RealmResults<Grade> element) {
                //updateDisplayList();
            }
        });
    }

    public void onStop() {
        categoryRealmResults.removeChangeListeners();
        gradeRealmResults.removeChangeListeners();
        realm.close();
    }

    private void setLessonGroupList() {
        List<LessonGroup> lessonGroups = new ArrayList<>();
        for (Category category : categoryRealmResults) {
            List<LessonParcelable> lessonParcelables = new ArrayList<>();
            for (Lesson lesson : category.getLessons().sort(Constants.COL_SEQ)) {
                if (lesson.getQuestions() != null && lesson.getQuestions().size() > 0) {
                    LessonParcelable lessonParcelable = new LessonParcelable(
                            lesson.getId(), lesson.getTitle());
                    lessonParcelables.add(lessonParcelable);
                }
            }
            LessonGroup lessonGroup = new LessonGroup(category.getTitle(), lessonParcelables);
            lessonGroups.add(lessonGroup);
        }
        getView().setLessonGroupList(lessonGroups);
    }

    private void updateDisplayList() {
        List<DisplayGrade> displayGrades = new ArrayList<>();
        int x = 0;
        if (categoryRealmResults.isLoaded() && categoryRealmResults.isValid()) {
            for (Category category : categoryRealmResults) {
                x++;
                DisplayGrade displayGradeCategory = new DisplayGrade();
                displayGradeCategory.setSequence(x);
                displayGradeCategory.setView(DisplayGrade.VIEW_CATEGORY);
                displayGradeCategory.setTitle(category.getTitle());
                displayGrades.add(displayGradeCategory);
                for (Lesson lesson : category.getLessons().sort(Constants.COL_SEQ)) {
                    // set lesson grades
                    x++;
                    DisplayGrade displayGradeLesson = new DisplayGrade();
                    displayGradeLesson.setSequence(x);
                    displayGradeLesson.setView(DisplayGrade.VIEW_LESSON);
                    displayGradeLesson.setTitle(lesson.getTitle());
                    List<Grade> grades = new ArrayList<>();
                    if (gradeRealmResults.isLoaded() && gradeRealmResults.isValid()) {
                        RealmResults<Grade> lessonGradeRealmResults = gradeRealmResults.where()
                                .equalTo("lesson", lesson.getId())
                                .findAllSorted("tryCount", Sort.ASCENDING);
                        grades = realm.copyFromRealm(lessonGradeRealmResults);
                    }
                    displayGradeLesson.setGrades(grades);
                    displayGrades.add(displayGradeLesson);
                }
            }
        }
        getView().setDisplayGradeList(displayGrades);
    }

    public void refreshGrades() {
        getView().startLoading();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", user.getUsername());
        App.getInstance().getApiInterface().getGrades(
                Credentials.basic(user.getUsername(), user.getPassword()), parameters)
                .enqueue(new Callback<List<Grade>>() {
                    @Override
                    public void onResponse(Call<List<Grade>> call, final Response<List<Grade>> response) {
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Grade.class);
                                    for (Grade grade : response.body()) {
                                        grade.setSaved(true);
                                        realm.insertOrUpdate(grade);
                                    }
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    Log.e(TAG, "onError: Error Saving Grades List", error);
                                    getView().showAlert("Error Saving Grades List");
                                }
                            });
                        } else {
                            try {
                                getView().showAlert(response.errorBody().string());
                            } catch (IOException e) {
                                Log.e(TAG, "onResponse: Error parsing error body", e);
                                getView().showAlert(response.message() != null ? response.message()
                                        : "Unknown Error");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Grade>> call, Throwable t) {
                        Log.e(TAG, "onFailure: API call", t);
                        getView().stopLoading();
                        getView().showAlert("Error Calling API");
                    }
                });
    }

    public void saveGrade(Grade grade) {
        super.saveGrade(grade, Credentials.basic(user.getUsername(), user.getPassword()));
    }


}
