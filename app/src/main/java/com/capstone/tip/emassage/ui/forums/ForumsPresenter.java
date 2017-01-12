package com.capstone.tip.emassage.ui.forums;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.model.response.ForumListResponse;
import com.capstone.tip.emassage.ui.base.VotePresenter;

import java.io.IOException;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class ForumsPresenter extends VotePresenter<ForumsView> {

    private static final String TAG = ForumsPresenter.class.getSimpleName();
    private Realm realm;
    private RealmResults<Forum> forumRealmResults;
    private User user;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        forumRealmResults = realm.where(Forum.class).findAllSortedAsync("created", Sort.DESCENDING);
        forumRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Forum>>() {
            @Override
            public void onChange(RealmResults<Forum> element) {
                if (forumRealmResults.isLoaded() && forumRealmResults.isValid())
                    getView().setForums(realm.copyFromRealm(forumRealmResults));
            }
        });
    }

    public void onStop() {
        forumRealmResults.removeChangeListeners();
        realm.close();
    }

    public void loadForumList() {
        forums(App.getInstance().getApiInterface().forums());
    }

    public void loadForumList(Map<String, String> params) {
        forums(App.getInstance().getApiInterface().forums(params));
    }

    private void forums(Call<ForumListResponse> forumListResponseCall) {
        forumListResponseCall.enqueue(new Callback<ForumListResponse>() {
            @Override
            public void onResponse(Call<ForumListResponse> call, final Response<ForumListResponse> response) {
                Log.d(TAG, "onResponse: ");
                getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if (response.body().getPrevious() == null
                                    || response.body().getPrevious().isEmpty())
                                realm.delete(Forum.class);
                            realm.insertOrUpdate(response.body().getResults());
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
                            Log.e(TAG, "onError: Error Saving Forum List", error);
                            getView().showMessage("Error Saving Forum List");
                        }
                    });
                    if (response.body().getCount() <= 0)
                        getView().showMessage("No Forums Retrieved");
                    getView().addNext(response.body().getNext());
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
            public void onFailure(Call<ForumListResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Forum List Failed", t);
                getView().stopLoading();
                getView().showMessage("Error Retrieving Forum List");
            }
        });
    }

    public void vote(int forumId, int vote) {
        super.vote(realm.copyFromRealm(user), forumId, vote);
    }

}
