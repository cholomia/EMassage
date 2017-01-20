package com.capstone.tip.emassage.ui.forums;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.model.response.ForumListResponse;
import com.capstone.tip.emassage.ui.base.VotePresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

class ForumsPresenter extends VotePresenter<ForumsView> {

    private static final String TAG = ForumsPresenter.class.getSimpleName();
    private Realm realm;
    private RealmResults<Forum> forumRealmResults;
    private User user;
    private String query;

    public void onStart() {
        query = "";
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        forumRealmResults = realm.where(Forum.class).findAllSortedAsync("points", Sort.DESCENDING);
        forumRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Forum>>() {
            @Override
            public void onChange(RealmResults<Forum> element) {
                filterList();
            }
        });
    }

    public void onStop() {
        forumRealmResults.removeChangeListeners();
        realm.close();
    }

    void loadForumList() {
        forums(App.getInstance().getApiInterface().forums(Credentials.basic(user.getUsername(), user.getPassword())));
    }

    void loadForumList(Map<String, String> params) {
        forums(App.getInstance().getApiInterface().forums(Credentials.basic(user.getUsername(), user.getPassword()), params));
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

    void setQuery(String query) {
        this.query = query;
        filterList();
    }

    private void filterList() {
        if (forumRealmResults.isLoaded() && forumRealmResults.isValid()) {
            List<Forum> forumList;
            if (query != null && !query.isEmpty()) {
                forumList = realm.copyFromRealm(forumRealmResults.where()
                        .contains("title", query, Case.INSENSITIVE)
                        .or()
                        .contains("content", query, Case.INSENSITIVE)
                        .findAll());
            } else {
                forumList = realm.copyFromRealm(forumRealmResults);
            }
            getView().setForums(forumList);
        }

    }

}
