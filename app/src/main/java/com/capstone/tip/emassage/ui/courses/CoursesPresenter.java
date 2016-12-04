package com.capstone.tip.emassage.ui.courses;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.model.data.Course;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class CoursesPresenter extends MvpNullObjectBasePresenter<CoursesView> {
    private static final String TAG = CoursesPresenter.class.getSimpleName();

    public void refresh() {
        getView().startLoading();
        App.getInstance().getApiInterface().courses().enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, final Response<List<Course>> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
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
                            Log.e(TAG, "onError: Error Saving Course List", error);
                            getView().showAlert("Error Saving Course List");
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
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e(TAG, "onFailure: API call", t);
                getView().stopLoading();
                getView().showAlert("Error Calling API");
            }
        });
    }
}
