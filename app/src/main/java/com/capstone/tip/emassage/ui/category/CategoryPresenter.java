package com.capstone.tip.emassage.ui.category;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.model.data.Lesson;
import com.capstone.tip.emassage.model.pojo.LessonGroup;
import com.capstone.tip.emassage.model.pojo.LessonParcelable;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class CategoryPresenter extends MvpNullObjectBasePresenter<CategoryView> {

    private Realm realm;
    private RealmResults<Category> categoryRealmResults;

    public void refresh() {
        getView().startLoading();
        App.getInstance().getApiInterface().category().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, final Response<List<Category>> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(Category.class);
                            realm.copyToRealmOrUpdate(response.body());
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
                            error.printStackTrace();
                            getView().showAlert("Error Saving Course List");
                        }
                    });
                } else {
                    try {
                        getView().showAlert(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        getView().showAlert(response.message() != null ? response.message()
                                : "Unknown Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                t.printStackTrace();
                getView().stopLoading();
                getView().showAlert("Error Calling API");
            }
        });
    }

    public void onStart() {
        realm = Realm.getDefaultInstance();
        categoryRealmResults = realm.where(Category.class).findAllSorted(Constants.COL_SEQ);
        categoryRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Category>>() {
            @Override
            public void onChange(RealmResults<Category> element) {
                setLessonGroup(categoryRealmResults);
            }
        });
    }

    private void setLessonGroup(List<Category> categories) {
        List<LessonGroup> lessonGroups = new ArrayList<>();
        for (Category category : categories) {
            List<LessonParcelable> lessonParcelables = new ArrayList<>();
            for (Lesson lesson : category.getLessons().sort(Constants.COL_SEQ)) {
                LessonParcelable lessonParcelable = new LessonParcelable(
                        lesson.getId(), lesson.getTitle());
                lessonParcelables.add(lessonParcelable);
            }
            LessonGroup lessonGroup = new LessonGroup(category.getTitle(), lessonParcelables);
            lessonGroups.add(lessonGroup);
        }
        getView().setLessonGroups(lessonGroups);
    }

    public void onStop() {
        categoryRealmResults.removeChangeListeners();
        realm.close();
    }
}
