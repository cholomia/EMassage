package com.capstone.tip.emassage.ui.text_twist;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.model.data.Twist;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 1/22/2017.
 */

public class TextTwistPresenter extends MvpNullObjectBasePresenter<TextTwistView> {
    private static final String TAG = TextTwistPresenter.class.getSimpleName();
    private Realm realm;
    private RealmResults<Twist> twistRealmResults;

    void onStart() {
        realm = Realm.getDefaultInstance();
        twistRealmResults = realm.where(Twist.class).findAllAsync();
        twistRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Twist>>() {
            @Override
            public void onChange(RealmResults<Twist> element) {
                if (twistRealmResults.isLoaded() && twistRealmResults.isValid()) {

                }
            }
        });
    }

    void onStop() {
        twistRealmResults.removeChangeListeners();
        realm.close();
    }

    public void refresh() {
        getView().startLoading();
        App.getInstance().getApiInterface().getTwistWords().enqueue(new Callback<List<Twist>>() {
            @Override
            public void onResponse(Call<List<Twist>> call, final Response<List<Twist>> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(Twist.class);
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
                            Log.e(TAG, "onError: Error Saving Text Twist List", error);
                            getView().showAlert("Error Saving Text Twist List");
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
            public void onFailure(Call<List<Twist>> call, Throwable t) {
                Log.e(TAG, "onFailure: API call", t);
                getView().stopLoading();
                getView().showAlert("Error Calling API");
            }
        });
    }
}
