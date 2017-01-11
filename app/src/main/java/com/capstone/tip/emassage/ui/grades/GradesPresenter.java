package com.capstone.tip.emassage.ui.grades;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.model.data.Course;
import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.model.data.Lesson;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.model.pojo.DisplayGrade;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pocholomia
 * @since 10/01/2017
 */

public class GradesPresenter extends MvpNullObjectBasePresenter<GradesView> {

    private static final String TAG = GradesPresenter.class.getSimpleName();

    private Realm realm;
    private RealmResults<Course> courseRealmResults;
    private RealmResults<Grade> gradeRealmResults;
    private User user;

    public void onStart() {
        realm = Realm.getDefaultInstance();

        user = realm.where(User.class).findFirst();

        courseRealmResults = realm.where(Course.class).findAllSortedAsync(Constants.COL_SEQ);
        courseRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Course>>() {
            @Override
            public void onChange(RealmResults<Course> element) {
                updateDisplayList();
            }
        });

        gradeRealmResults = realm.where(Grade.class).findAllAsync();
        gradeRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Grade>>() {
            @Override
            public void onChange(RealmResults<Grade> element) {
                updateDisplayList();
            }
        });
    }

    public void onStop() {
        courseRealmResults.removeChangeListeners();
        gradeRealmResults.removeChangeListeners();
        realm.close();
    }

    private void updateDisplayList() {
        List<DisplayGrade> displayGrades = new ArrayList<>();
        int x = 0;
        if (courseRealmResults.isLoaded() && courseRealmResults.isValid()) {
            for (Course course : courseRealmResults) {
                // set course header
                x++;
                DisplayGrade displayGradeCourse = new DisplayGrade();
                displayGradeCourse.setSequence(x);
                displayGradeCourse.setView(DisplayGrade.VIEW_COURSE);
                displayGradeCourse.setTitle(course.getTitle());
                displayGrades.add(displayGradeCourse);
                for (Category category : course.getCategories()) {
                    // set category headers
                    x++;
                    DisplayGrade displayGradeCategory = new DisplayGrade();
                    displayGradeCategory.setSequence(x);
                    displayGradeCategory.setView(DisplayGrade.VIEW_CATEGORY);
                    displayGradeCategory.setTitle(category.getTitle());
                    displayGrades.add(displayGradeCategory);
                    for (Lesson lesson : category.getLessons()) {
                        // set lesson grades
                        x++;
                        DisplayGrade displayGradeLesson = new DisplayGrade();
                        displayGradeLesson.setSequence(x);
                        displayGradeLesson.setView(DisplayGrade.VIEW_LESSON);
                        displayGradeLesson.setTitle(lesson.getTitle());
                        if (gradeRealmResults.isLoaded() && gradeRealmResults.isValid()) {
                            Grade grade = gradeRealmResults.where()
                                    .equalTo("lesson", lesson.getId()).findFirst();
                            if (grade != null) {
                                displayGradeLesson.setGrade(realm.copyFromRealm(grade));
                            }
                        }
                        displayGrades.add(displayGradeLesson);
                    }
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
                                        grade.setLocal(false);
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

    public void saveGrade(final Grade grade) {
        Call<Grade> gradeCall = !grade.isSaved() && !grade.isLocal() ?
                App.getInstance().getApiInterface().saveGrade(grade.getId(),
                        Credentials.basic(user.getUsername(), user.getPassword()),
                        grade.getLesson(), grade.getRawScore(), grade.getItemCount())
                : App.getInstance().getApiInterface().saveGrade(
                Credentials.basic(user.getUsername(), user.getPassword()),
                grade.getLesson(), grade.getRawScore(), grade.getItemCount());
        getView().startLoading();
        gradeCall.enqueue(new Callback<Grade>() {
            @Override
            public void onResponse(Call<Grade> call, final Response<Grade> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Grade mGrade = realm.where(Grade.class).equalTo("id", grade.getId()).findFirst();
                            mGrade.deleteFromRealm();
                            Grade grade1 = realm.copyToRealmOrUpdate(response.body());
                            grade1.setSaved(true);
                            grade1.setLocal(false);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            realm.close();
                            getView().showAlert("Save Grade Successful");
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            realm.close();
                            Log.e(TAG, "onError: Error Saving Grade", error);
                            getView().showAlert("Error Saving Grade");
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
            public void onFailure(Call<Grade> call, Throwable t) {
                Log.e(TAG, "onFailure: API call", t);
                getView().stopLoading();
                getView().showAlert("Error Calling API");
            }
        });

    }
}