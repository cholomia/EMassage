package com.capstone.tip.emassage.ui.base;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.model.response.ForumVote;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pocholomia
 * @since 04/01/2017
 */

public class VotePresenter<V extends VoteView> extends MvpNullObjectBasePresenter<V> {

    private static final String TAG = VotePresenter.class.getSimpleName();

    public void vote(User user, final int forumId, int vote) {
        getView().startProgressLoading();
        App.getInstance().getApiInterface().forumVote("f-" + forumId + "-" + user.getUsername(),
                Credentials.basic(user.getUsername(), user.getPassword()), forumId, vote)
                .enqueue(new Callback<ForumVote>() {
                    @Override
                    public void onResponse(Call<ForumVote> call, final Response<ForumVote> response) {
                        getView().stopProgressDialog();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(response.body().getForum());
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
                                    Log.e(TAG, "onError: Error Saving ForumVote", error);
                                    getView().showMessage("Error Saving ForumVote");
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
                    public void onFailure(Call<ForumVote> call, Throwable t) {
                        Log.e(TAG, "onFailure: API call", t);
                        getView().stopProgressDialog();
                        getView().showMessage("Error Calling API");
                    }
                });
    }

}
