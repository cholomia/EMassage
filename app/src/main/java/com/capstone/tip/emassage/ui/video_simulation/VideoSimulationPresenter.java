package com.capstone.tip.emassage.ui.video_simulation;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.model.data.Video;
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
 * @author pocholomia
 * @since 17/01/2017
 */

public class VideoSimulationPresenter extends MvpNullObjectBasePresenter<VideoSimulationView> {
    private static final String TAG = VideoSimulationPresenter.class.getSimpleName();
    private Realm realm;
    private RealmResults<Video> videoRealmResults;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        videoRealmResults = realm.where(Video.class).findAllAsync();
        videoRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Video>>() {
            @Override
            public void onChange(RealmResults<Video> element) {
                getView().setVideoList(realm.copyFromRealm(videoRealmResults));
            }
        });
    }

    public void onStop() {
        videoRealmResults.removeChangeListeners();
        realm.close();
    }

    public void onRefresh() {
        App.getInstance().getApiInterface().videos().enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, final Response<List<Video>> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(Video.class);
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
                            Log.e(TAG, "onError: Error Saving Video List", error);
                            getView().showMessage("Error Saving Video List");
                        }
                    });

                } else {
                    try {
                        getView().showMessage(response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: Error parsing error body", e);
                        getView().showMessage(response.message() != null ? response.message()
                                : "Unknown Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e(TAG, "onFailure: Video List Failed", t);
                getView().stopLoading();
                getView().showMessage("Error Retrieving Video List");
            }
        });
    }
}
