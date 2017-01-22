package com.capstone.tip.emassage.ui.base;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.ui.grades.GradesPresenter;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pocholomia
 * @since 12/01/2017
 */

public abstract class GradesSavePresenter<T extends GradesSaveView> extends MvpNullObjectBasePresenter<T> {

    private static final String TAG = GradesPresenter.class.getSimpleName();

    public void saveGrade(final Grade grade, String basicAuthentication) {
        getView().startLoading();
        App.getInstance().getApiInterface().saveGrade(grade.getId(), basicAuthentication, grade.getId(),
                grade.getLesson(), grade.getRawScore(), grade.getItemCount(), grade.getTryCount())
                .enqueue(new Callback<Grade>() {
                    @Override
                    public void onResponse(Call<Grade> call, final Response<Grade> response) {
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Grade grade = realm.copyToRealmOrUpdate(response.body());
                                    grade.setSaved(true);
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
